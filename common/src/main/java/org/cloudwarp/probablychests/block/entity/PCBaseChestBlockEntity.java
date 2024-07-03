package org.cloudwarp.probablychests.block.entity;

import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PCBaseChestBlockEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity {
    public static final RawAnimation CLOSE = RawAnimation.begin().thenLoop("closed");
    public static final RawAnimation OPEN = RawAnimation.begin().thenLoop("opened");
    public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public boolean isMimic = false;
    public boolean isNatural = false;
    public boolean hasBeenInteractedWith = false;
    public boolean hasMadeMimic = false;

    public boolean hasGoldLock = false;
    public boolean hasVoidLock = false;
    public boolean hasIronLock = false;
    public boolean isLocked = false;
    public UUID owner = null;
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            PCBaseChestBlockEntity.playSound(world, pos, state, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            PCBaseChestBlockEntity.playSound(world, pos, state, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            PCBaseChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof PCChestScreenHandler) {
                Container inventory = ((PCChestScreenHandler) player.containerMenu).getInventory();
                return inventory == PCBaseChestBlockEntity.this;
            }
            return false;
        }
    };
    PCChestTypes type;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);

    public PCBaseChestBlockEntity(PCChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    public static int getPlayersLookingInChestCount(BlockGetter world, BlockPos pos) {
        BlockEntity blockEntity;
        BlockState blockState = world.getBlockState(pos);
        if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(pos)) instanceof PCBaseChestBlockEntity) {
            return ((PCBaseChestBlockEntity) blockEntity).stateManager.getOpenerCount();
        }
        return 0;
    }

    public static void copyInventory(PCBaseChestBlockEntity from, PCBaseChestBlockEntity to) {
        NonNullList<ItemStack> defaultedList = from.getItems();
        from.setItems(to.getItems());
        to.setItems(defaultedList);
    }

    public static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 0.5;
        double f = (double) pos.getZ() + 0.5;

        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
    }

    public static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent, float pitchRange) {
        double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 0.5;
        double f = (double) pos.getZ() + 0.5;

        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + pitchRange);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
        }
        this.isMimic = nbt.getBoolean("isMimic");
        this.hasGoldLock = nbt.getBoolean("hasGoldLock");
        this.hasVoidLock = nbt.getBoolean("hasVoidLock");
        this.hasIronLock = nbt.getBoolean("hasIronLock");
        this.isLocked = nbt.getBoolean("isLocked");
        this.isNatural = nbt.getBoolean("isNatural");
        this.hasBeenInteractedWith = nbt.getBoolean("hasBeenOpened");
        this.hasMadeMimic = nbt.getBoolean("hasMadeMimic");
        if (nbt.contains("pc_owner")) {
            this.owner = nbt.getUUID("pc_owner");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
        }
        nbt.putBoolean("isMimic", this.isMimic);
        nbt.putBoolean("hasGoldLock", this.hasGoldLock);
        nbt.putBoolean("hasVoidLock", this.hasVoidLock);
        nbt.putBoolean("hasIronLock", this.hasIronLock);
        nbt.putBoolean("isLocked", this.isLocked);
        nbt.putBoolean("isNatural", this.isNatural);
        nbt.putBoolean("hasBeenOpened", this.hasBeenInteractedWith);
        nbt.putBoolean("hasMadeMimic", this.hasMadeMimic);
        if (this.owner != null) {
            nbt.putUUID("pc_owner", this.owner);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public void onScheduledTick() {
        if (!this.remove) {
            this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }


    protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();
        world.blockEvent(pos, block, 1, newViewerCount);
        if (oldViewerCount != newViewerCount) {
            if (newViewerCount > 0) {
                world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, PCChestState.OPENED));
            } else {
                world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, PCChestState.CLOSED));
            }
        }
    }


    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            return true;
        }
        return super.triggerEvent(type, data);
    }

    public PCChestState getChestState() {
        return this.getBlockState().getValue(PCBaseChestBlockEntity.CHEST_STATE);
    }

    public void setChestState(PCChestState state) {
        this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(CHEST_STATE, state));
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        if (!hasBeenInteractedWith && player.isSpectator()) {
            return null;
        }
        if (this.canOpen(player)) {
            unpackLootTable(inventory.player);
            return PCChestScreenHandler.createScreenHandler(syncId, inventory, this);
        }
        return null;
    }

    private PlayState predicate(AnimationState<PCBaseChestBlockEntity> state) {
        return switch (state.getAnimatable().getChestState()) {
            case CLOSED -> {
                state.getController().setOverrideEasingType(EasingType.EASE_OUT_SINE);
                yield state.setAndContinue(CLOSE);
            }
            case OPENED -> state.setAndContinue(OPEN);
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 7, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return PCChestScreenHandler.createScreenHandler(syncId, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public int getContainerSize() {
        return 54;
    }

    public PCChestTypes type() {
        return type;
    }


}
