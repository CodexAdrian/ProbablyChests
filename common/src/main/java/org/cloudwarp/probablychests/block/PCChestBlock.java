package org.cloudwarp.probablychests.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCLockedState;
import org.cloudwarp.probablychests.utils.VoxelShaper;

import java.util.Map;

import static org.cloudwarp.probablychests.utils.PCMimicCreationUtils.*;

public class PCChestBlock extends AbstractChestBlock<PCBaseChestBlockEntity> implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;
    public static final EnumProperty<PCLockedState> LOCKED_STATE = PCProperties.PC_LOCKED_STATE;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.5D, 15.0D, 14.0D, 14.5D);
    protected static final Map<Direction, VoxelShape> SHAPES = VoxelShaper.generateRotations(SHAPE);
    private static final MapCodec<PCChestBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            PCChestTypes.CODEC.fieldOf("type").forGetter(PCChestBlock::getType)
    ).apply(instance, PCChestBlock::new));

    private final PCChestTypes type;

    public PCChestBlock(Properties settings, PCChestTypes type) {
        super(settings, type::getBlockEntityType);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, false).setValue(CHEST_STATE, PCChestState.CLOSED).setValue(LOCKED_STATE, PCLockedState.UNLOCKED));
        this.type = type;
    }

    public static boolean isChestBlocked(LevelAccessor world, BlockPos pos) {
        return PCChestBlock.hasBlockOnTop(world, pos);
    }

    public static boolean isDry(BlockState state) {
        return !state.getValue(WATERLOGGED);
    }

    private static boolean hasBlockOnTop(BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return world.getBlockState(blockPos).isRedstoneConductor(world, blockPos);
    }

    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!getChestBlockFromWorld(world, pos).isLocked) {
            tryMakeHostileMimic(world, pos, state, player, this.type);
        }
    }

    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof Player player && !getChestBlockFromWorld(world, pos).isLocked) {
            tryMakeHostileMimic(world, pos, state, player, this.type);
        }
    }

    @Override
    public float defaultDestroyTime() {
        if (this.stateDefinition.getProperty(PCProperties.PC_LOCKED_STATE.getName()).equals(PCLockedState.LOCKED)) {
            return -1F;
        }
        return 2.0F;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        float f = state.getValue(PCProperties.PC_LOCKED_STATE).equals(PCLockedState.LOCKED) ? -1F : 2.0F;
        if (f == -1.0f) {
            return 0.0f;
        }
        int i = player.hasCorrectToolForDrops(state) ? 30 : 100;
        return player.getDestroySpeed(state) / f / (float) i;
    }

    public boolean unlockBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
        if (ProbablyChests.loadedConfig.chestSettings.enableLockedChestOwners && chest.owner != null && !player.getUUID().equals(chest.owner)) {
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK2, 1.0f);
            return false;
        }
        NonNullList<ItemStack> locks = NonNullList.create();
        ItemStack itemStack = player.getItemInHand(hand);
        if (chest.hasGoldLock && itemStack.is(PCItems.GOLD_KEY.get())) {
            chest.isLocked = false;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3f);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(PCItems.GOLD_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }
                chest.hasGoldLock = false;
            }
            return true;
        } else if (chest.hasVoidLock && itemStack.is(PCItems.VOID_KEY.get())) {
            chest.isLocked = false;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3f);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(PCItems.VOID_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }
                chest.hasVoidLock = false;
            }
            return true;
        } else if (chest.hasIronLock && itemStack.is(PCItems.IRON_KEY.get())) {
            chest.isLocked = false;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3f);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(PCItems.IRON_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }
                chest.hasIronLock = false;
            }
            return true;
        } else {
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK2, 1.0f);
            return false;
        }
    }

    public boolean lockBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
        if (ProbablyChests.loadedConfig.chestSettings.enableLockedChestOwners && chest.owner != null && !player.getUUID().equals(chest.owner)) {
            return false;
        }
        ItemStack itemStack = player.getItemInHand(hand);
        if (chest.hasGoldLock && itemStack.is(PCItems.GOLD_KEY.get())) {
            chest.isLocked = true;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6f);
            return true;
        } else if (chest.hasVoidLock && itemStack.is(PCItems.VOID_KEY.get())) {
            chest.isLocked = true;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6f);
            return true;
        } else if (chest.hasIronLock && itemStack.is(PCItems.IRON_KEY.get())) {
            chest.isLocked = true;
            PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6f);
            return true;
        } else {
            return false;
        }
    }

    public boolean addLockToBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!ProbablyChests.loadedConfig.chestSettings.allowChestLocking) {
            return false;
        }
        PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
        ItemStack itemStack = player.getItemInHand(hand);
        if (!chest.hasGoldLock && !chest.hasVoidLock && !chest.hasIronLock) {
            if (itemStack.is(PCItems.GOLD_LOCK.get()) && chest.type().equals(PCChestTypes.GOLD)) {
                chest.isLocked = true;
                chest.hasGoldLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6f);
                return true;
            } else if (itemStack.is(PCItems.VOID_LOCK.get()) && chest.type().equals(PCChestTypes.SHADOW)) {
                chest.isLocked = true;
                chest.hasVoidLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6f);
                return true;
            } else if (itemStack.is(PCItems.IRON_LOCK.get())) {
                chest.isLocked = true;
                chest.hasIronLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                PCBaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6f);
                return true;
            }
        }
        return false;
    }

    public void lockBlockState(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, state.setValue(PCProperties.PC_LOCKED_STATE, PCLockedState.LOCKED));
    }

    public void unlockBlockState(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, state.setValue(PCProperties.PC_LOCKED_STATE, PCLockedState.UNLOCKED));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
        PCConfig config = ProbablyChests.loadedConfig;
        ItemStack itemStack = player.getItemInHand(hand);
        if (chest.isLocked) {
            if (unlockBlock(state, world, pos, player, hand, hit)) {
                unlockBlockState(state, world, pos);
                return ItemInteractionResult.CONSUME;
            } else {
                return ItemInteractionResult.FAIL;
            }
        } else {
            if (addLockToBlock(state, world, pos, player, hand, hit)) {
                lockBlockState(state, world, pos);
                return ItemInteractionResult.CONSUME;
            }
            if (lockBlock(state, world, pos, player, hand, hit)) {
                lockBlockState(state, world, pos);
                return ItemInteractionResult.CONSUME;
            }
        }
        //------------------------
        if (chest != null) {
            if (itemStack.is(PCItems.PET_MIMIC_KEY.get()) && config.mimicSettings.allowPetMimics && !player.isShiftKeyDown() && tryMakePetMimic(world, pos, state, player, this.type)) {
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return ItemInteractionResult.CONSUME;
            } else if (itemStack.is(PCItems.MIMIC_KEY.get()) && !player.isShiftKeyDown() && !isSecretMimic(chest, world, pos, this.type) && world.getDifficulty() != Difficulty.PEACEFUL) {
                chest.isMimic = true;
                chest.isNatural = false;
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return ItemInteractionResult.CONSUME;
            } else {
                if (isSecretMimic(chest, world, pos, type)) {
                    tryMakeHostileMimic(world, pos, state, player, this.type);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        MenuProvider namedScreenHandlerFactory = this.getMenuProvider(state, world, pos);
        if (namedScreenHandlerFactory != null && player instanceof ServerPlayer && chest.canOpen(player)) {
            player.openMenu(namedScreenHandlerFactory);
            player.awardStat(this.getOpenStat());
        }
        return ItemInteractionResult.CONSUME;
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
        if (chest == null) {
            super.onRemove(state, world, pos, newState, moved);
            return;
        }
        if (!isSecretMimic(chest, world, pos, this.type)) {
            if (blockEntity instanceof Container inventory) {
                Containers.dropContents(world, pos, inventory);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        } else {
            tryMakeHostileMimic(world, pos, state, null, this.type);
        }
        NonNullList<ItemStack> locks = NonNullList.create();
        if (chest.hasVoidLock) {
            locks.add(new ItemStack(PCItems.VOID_LOCK.get()));
        } else if (chest.hasGoldLock) {
            locks.add(new ItemStack(PCItems.GOLD_LOCK.get()));
        } else if (chest.hasIronLock) {
            locks.add(new ItemStack(PCItems.IRON_LOCK.get()));
        }
        if (!locks.isEmpty()) {
            Containers.dropContents(world, pos, locks);
        }

        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }


    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return (this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite())).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CHEST_STATE, LOCKED_STATE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PCBaseChestBlockEntity chestBlockEntity) {
            chestBlockEntity.onScheduledTick();
        }
    }

    public BlockEntityType<? extends PCBaseChestBlockEntity> getExpectedEntityType() {
        return this.blockEntityType.get();
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }


    @Override
    protected MapCodec<? extends AbstractChestBlock<PCBaseChestBlockEntity>> codec() {
        return CODEC;
    }

    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level world, BlockPos pos, boolean ignoreBlocked) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    public PCChestTypes getType() {
        return type;
    }

}