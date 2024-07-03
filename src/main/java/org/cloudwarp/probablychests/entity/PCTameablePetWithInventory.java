package org.cloudwarp.probablychests.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.entity.ai.MimicMoveControl;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.network.MimicInventoryPacket;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

import static org.cloudwarp.probablychests.utils.PCMimicCreationUtils.convertHostileMimicToPet;
import static org.cloudwarp.probablychests.utils.PCMimicCreationUtils.convertPetMimicToHostile;

public abstract class PCTameablePetWithInventory extends TamableAnimal implements OwnableEntity, ContainerListener, NeutralMob {

    public SimpleContainer inventory = new SimpleContainer(54);
    public boolean interacting;
    PCChestTypes type;
    @Nullable
    private UUID angryAt;

    protected static final EntityDataAccessor<MimicState> MIMIC_STATE;
    protected static final EntityDataAccessor<Integer> ANGER_TIME;
    protected static final UniformInt ANGER_TIME_RANGE;
    protected static final EntityDataAccessor<Boolean> IS_ABANDONED;
    protected static final EntityDataAccessor<Boolean> MIMIC_HAS_LOCK;
    protected static final EntityDataAccessor<Boolean> IS_MIMIC_LOCKED;
    protected static final EntityDataAccessor<Boolean> IS_OPEN_STATE;


    static {
        MIMIC_STATE = SynchedEntityData.defineId(PCTameablePetWithInventory.class, PCEntities.STATE_SERIALIZER);
        ANGER_TIME = SynchedEntityData.defineId(PCTameablePetWithInventory.class, EntityDataSerializers.INT);
        IS_ABANDONED = SynchedEntityData.defineId(PCTameablePetWithInventory.class, EntityDataSerializers.BOOLEAN);
        MIMIC_HAS_LOCK = SynchedEntityData.defineId(PCTameablePetWithInventory.class, EntityDataSerializers.BOOLEAN);
        IS_MIMIC_LOCKED = SynchedEntityData.defineId(PCTameablePetWithInventory.class, EntityDataSerializers.BOOLEAN);
        IS_OPEN_STATE = SynchedEntityData.defineId(PCTameablePetWithInventory.class, EntityDataSerializers.BOOLEAN);
        ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    }

    public int viewerCount = 0;

    public int closeAnimationTimer;
    public int openAnimationTimer;
    public int biteAnimationTimer;
    public int isAbandonedTimer;
    public int biteDamageAmount = 2;

    private static final double moveSpeed = 1.0D;


    public PCTameablePetWithInventory(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
        this.type = PCChestTypes.NORMAL;
        this.noCulling = true;
        this.inventory.addListener(this);
        this.isAbandonedTimer = ProbablyChests.loadedConfig.mimicSettings.abandonedMimicTimer * 1200;
    }

    public void setType(PCChestTypes type) {
        this.type = type;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }


    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() != null && this.getOwner() instanceof ServerPlayer) {
            ((PlayerEntityAccess) this.getOwner()).removePetMimicFromOwnedList(this.getUUID());
        }
    }

    public void tick() {
        super.tick();
        if (this.getOwner() != null && !this.level().isClientSide() && this.isTame()) {
            if (this.getIsAbandoned()) {
                if (this.isAbandonedTimer > 0) {
                    this.isAbandonedTimer--;
                } else {
                    this.setTame(false, false);

                    convertPetMimicToHostile(level(), type, this);
                }
            }
        }
    }

    public boolean isFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && stack.is(ItemTags.WOLF_FOOD);
    }


    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        PCConfig config = ProbablyChests.loadedConfig;

        if (this.isTame()) {
            if (!this.getIsAbandoned() && this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                FoodProperties food = itemStack.get(DataComponents.FOOD);
                if (food != null) this.heal((float) food.nutrition());
                this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
                return InteractionResult.SUCCESS;
            } else if (itemStack.is(PCItems.MIMIC_HAND_BELL)) {
                if (!this.getIsAbandoned() && this.getOwner() == player) {
                    if (player.isShiftKeyDown()) {
                        ((PlayerEntityAccess) player).removeMimicFromKeepList(this.getUUID());
                        // 4 8
                        this.playSound(PCSounds.BELL_HIT_4, this.getSoundVolume(), 0.5F + getPitchOffset(0.1F));
                    } else {
                        ((PlayerEntityAccess) player).addMimicToKeepList(this.getUUID());
                        this.playSound(PCSounds.BELL_HIT_4, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
                    }
                } else {
                    if (this.getIsAbandoned() && !((PlayerEntityAccess) player).checkForMimicLimit()) {
                        this.tame(player);
                        this.setIsAbandoned(false);
                        this.isAbandonedTimer = ProbablyChests.loadedConfig.mimicSettings.abandonedMimicTimer * 1200;
                        ((PlayerEntityAccess) player).addPetMimicToOwnedList(this.getUUID());
                        this.playSound(PCSounds.BELL_HIT_2, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
                    }
                }
                return InteractionResult.SUCCESS;
            } else if (this.getOwner() == player && !this.getIsAbandoned() && itemStack.is(PCItems.IRON_LOCK) && !this.getMimicHasLock() && config.mimicSettings.allowPetMimicLocking) {
                this.setMimicHasLock(true);
                this.setIsMimicLocked(true);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.playSound(PCSounds.APPLY_LOCK1, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
            } else if (this.getOwner() == player && !this.getIsAbandoned() && itemStack.is(PCItems.IRON_KEY) && this.getMimicHasLock() && config.mimicSettings.allowPetMimicLocking) {
                this.setIsMimicLocked(!this.getIsMimicLocked());
                if (this.getIsMimicLocked()) {
                    this.playSound(PCSounds.LOCK_UNLOCK, this.getSoundVolume(), 1.3F + getPitchOffset(0.1F));
                } else {
                    this.playSound(PCSounds.LOCK_UNLOCK, this.getSoundVolume(), 0.6F + getPitchOffset(0.1F));
                }
            } else if (!this.getIsAbandoned()) {
                InteractionResult actionResult = super.mobInteract(player, hand);
                if (player.isShiftKeyDown()) {
                    if (this.isOwnedBy(player)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.updateSitting(player);
                        this.playSound(this.isOrderedToSit() ? this.getSitSound() : this.getStandSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget((LivingEntity) null);
                        return InteractionResult.SUCCESS;
                    }

                    return actionResult;
                } else {
                    if (this.isEffectiveAi()) {
                        if (this.getIsMimicLocked() && config.mimicSettings.allowPetMimicLocking) {
                            if (this.getOwner() == player) {
                                this.openGui(player);
                            } else {
                                this.bite(player);
                                this.biteAnimationTimer = 6;
                                this.setMimicState(MimicState.BITING);
                            }
                        } else {
                            this.openGui(player);
                        }
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
        } else if (itemStack.is(PCItems.PET_MIMIC_KEY)) {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            if (this instanceof PCChestMimicPet) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget((LivingEntity) null);
                this.setOrderedToSit(true);
                this.updateSitting(player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                convertHostileMimicToPet(level(), type, this, player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
        return InteractionResult.FAIL;
    }

    public void bite(LivingEntity target) {
        if (this.isAlive()) {
            if (target.hurt(this.damageSources().mobAttack(this), this.biteDamageAmount)) {
                this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
                this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
                this.doEnchantDamageEffects(this, target);
            }
        }
    }

    public float getPitchOffset(float range) {
        return (this.random.nextFloat() - this.random.nextFloat()) * range;
    }


    public void updateSitting(Player player) {
    }

    @Override
    public float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public void containerChanged(Container sender) {
    }

    public void openGui(Player player) {
        if (player.level() != null && !this.level().isClientSide()) {
            if (this.viewerCount == 0) {
                openAnimationTimer = 12;
                this.setIsOpenState(true);
                this.playSound(this.getOpenSound(), this.getSoundVolume(), 0.8F + getPitchOffset(0.1F));
                this.playSound(PCSounds.CLOSE_2, this.getSoundVolume(), 1.5F + getPitchOffset(0.1F));
            }
            this.viewerCount++;
            int syncId = player.openMenu(new MimicScreenHandlerFactory()).getAsInt();
            MimicInventoryPacket packet = new MimicInventoryPacket(
                    this.inventory.getContainerSize(),
                    this.getId(),
                    (byte) syncId
            );
            for (ServerPlayer p : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(p, packet);
            }
        }
    }

    public void closeGui(Player player) {
        if (player.level() != null && !this.level().isClientSide()) {
            viewerCount -= 1;
            if (viewerCount == 0) {
                closeAnimationTimer = 12;
                this.setIsOpenState(false);
                this.playSound(this.getCloseSound(), this.getSoundVolume(), 0.8F + getPitchOffset(0.1F));
                this.playSound(PCSounds.CLOSE_2, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
            }
            if (viewerCount < 0) {
                System.out.println("this should not happen but i added a check just in case. Viewer count of pet mimic is less than 0");
                viewerCount = 0;
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                this.spawnAtLocation(itemstack);
            }
        }
    }

    @Override
    public int getMaxFallDistance() {
        return 20;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        MobEffectInstance statusEffectInstance = this.getEffect(MobEffects.JUMP);
        float f = statusEffectInstance == null ? 0.0F : (float) (statusEffectInstance.getAmplifier() + 1);
        return Mth.ceil((fallDistance - 20.0F - f) * damageMultiplier);
    }


    public float getJumpSoundPitch() {
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
    }

    public SoundEvent getJumpSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getSitSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getStandSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getOpenSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getHurtSound() {
        return SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
    }

    protected SoundEvent getLandingSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 0.5;
        double f = (double) pos.getZ() + 0.5;

        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
    }


    protected float getActiveEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.625F * dimensions.height();
    }


    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        ListTag listnbt = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (itemstack.isEmpty()) continue;
            CompoundTag compoundnbt = new CompoundTag();
            compoundnbt.putByte("Slot", (byte) i);
            listnbt.add(itemstack.save(level().registryAccess(), compoundnbt));
        }
        nbt.put("Inventory", listnbt);
        nbt.putBoolean("is_abandoned", this.getIsAbandoned());
        nbt.putBoolean("mimic_has_lock", this.getMimicHasLock());
        nbt.putBoolean("is_mimic_locked", this.getIsMimicLocked());
        this.addPersistentAngerSaveData(nbt);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        ListTag listnbt = nbt.getList("Inventory", 10);
        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            this.inventory.setItem(j, ItemStack.parse(level().registryAccess(), compoundnbt).orElse(ItemStack.EMPTY));
        }
        this.readPersistentAngerSaveData(this.level(), nbt);
        this.setIsAbandoned(nbt.getBoolean("is_abandoned"));
        this.setMimicHasLock(nbt.getBoolean("mimic_has_lock"));
        this.setIsMimicLocked(nbt.getBoolean("is_mimic_locked"));
    }

    public void setMimicState(MimicState state) {
        this.entityData.set(MIMIC_STATE, state);
    }

    public MimicState getMimicState() {
        return this.entityData.get(MIMIC_STATE);
    }

    public void setIsOpenState(boolean state) {
        this.entityData.set(IS_OPEN_STATE, state);
    }

    public boolean getIsOpenState() {
        return this.entityData.get(IS_OPEN_STATE);
    }

    public void setIsAbandoned(boolean state) {
        this.entityData.set(IS_ABANDONED, state);
    }

    public boolean getIsAbandoned() {
        return this.entityData.get(IS_ABANDONED);
    }

    public void setMimicHasLock(boolean state) {
        this.entityData.set(MIMIC_HAS_LOCK, state);
    }

    public boolean getMimicHasLock() {
        return this.entityData.get(MIMIC_HAS_LOCK);
    }

    public void setIsMimicLocked(boolean state) {
        this.entityData.set(IS_MIMIC_LOCKED, state);
    }

    public boolean getIsMimicLocked() {
        return this.entityData.get(IS_MIMIC_LOCKED);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANGER_TIME, 0);
        builder.define(IS_ABANDONED, false);
        builder.define(MIMIC_HAS_LOCK, false);
        builder.define(IS_MIMIC_LOCKED, false);
        builder.define(IS_OPEN_STATE, false);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public boolean areInventoriesDifferent(Container other) {
        return this.inventory != other;
    }

    public int getRemainingPersistentAngerTime() {
        return (Integer) this.entityData.get(ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(ANGER_TIME, angerTime);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    private class MimicScreenHandlerFactory implements MenuProvider {
        private PCTameablePetWithInventory mimic() {
            return PCTameablePetWithInventory.this;
        }

        @Override
        public Component getDisplayName() {
            return this.mimic().getDisplayName();
        }

        @Override
        public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
            var mimicInv = this.mimic().inventory;
            PCMimicScreenHandler screenHandler = PCMimicScreenHandler.createScreenHandler(syncId, inv, mimicInv);
            screenHandler.setMimicEntity(this.mimic());
            return screenHandler;
        }
    }

    public int getTicksUntilNextJump() {
        return this.random.nextInt(40) + 5;
    }


    static class SwimmingGoal extends Goal {
        private final PCTameablePetWithInventory mimic;

        public SwimmingGoal(PCTameablePetWithInventory mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            mimic.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.mimic.isInWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof MimicMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.mimic.getRandom().nextFloat() < 0.8F) {
                this.mimic.getJumpControl().jump();
            }

            ((MimicMoveControl) this.mimic.getMoveControl()).move(4.2D);
        }
    }

    static class IdleGoal extends Goal {
        private final PCTameablePetWithInventory mimic;

        public IdleGoal(PCTameablePetWithInventory mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        public boolean canUse() {
            return !this.mimic.isPassenger();
        }

        public void tick() {

        }
    }

    static class SleepGoal extends Goal {
        private final PCTameablePetWithInventory mimic;

        public SleepGoal(PCTameablePetWithInventory mimic) {
            this.mimic = mimic;
        }

        public boolean canUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == MimicState.SLEEPING;
        }

        public boolean canContinueToUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == MimicState.SLEEPING;
        }

        public void tick() {
            lockToBlock(10F, 10F);
            ((MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYRot(), true);
        }

        public void lockToBlock(float maxYawChange, float maxPitchChange) {
            float r = Math.round(this.mimic.getYHeadRot() / 90F) * 90F;
            double x = this.mimic.getBlockX() - this.mimic.getX();
            double z = this.mimic.getBlockZ() - this.mimic.getZ();
            this.mimic.setYRot(this.changeAngle(this.mimic.getYRot(), r, maxYawChange));
        }

        private float changeAngle(float from, float to, float max) {
            float f = Mth.wrapDegrees(to - from);
            if (f > max) {
                f = max;
            }
            if (f < -max) {
                f = -max;
            }
            return from + f;
        }
    }

    static class FollowOwnerGoal extends Goal {
        private final PCTameablePetWithInventory mimic;
        private final LevelReader world;
        private final PathNavigation navigation;
        private final float maxDistance;
        private final float minDistance;
        private final boolean leavesAllowed;
        private LivingEntity owner;
        private int updateCountdownTicks;
        private float oldWaterPathfindingPenalty;

        public FollowOwnerGoal(PCTameablePetWithInventory mimic, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
            this.mimic = mimic;
            this.world = mimic.level();
            this.navigation = mimic.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
            if (!(mimic.getNavigation() instanceof GroundPathNavigation) && !(mimic.getNavigation() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingEntity = this.mimic.getOwner();
            if (livingEntity == null) {
                return false;
            } else if (livingEntity.isSpectator()) {
                return false;
            } else if (this.mimic.isOrderedToSit()) {
                return false;
            } else if (this.mimic.distanceToSqr(livingEntity) < (double) (this.minDistance * this.minDistance)) {
                return false;
            } else if (this.mimic.getIsAbandoned()) {
                return false;
            } else {
                this.owner = livingEntity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.mimic.isOrderedToSit()) {
                return false;
            } else if (this.mimic.getIsAbandoned()) {
                return false;
            } else {
                return !(this.mimic.distanceToSqr(this.owner) <= (double) (this.maxDistance * this.maxDistance));
            }
        }

        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.mimic.getPathfindingMalus(PathType.WATER);
            this.mimic.setPathfindingMalus(PathType.WATER, 0.0F);
            this.mimic.setTarget(this.owner);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.mimic.setPathfindingMalus(PathType.WATER, this.oldWaterPathfindingPenalty);
        }

        public void tick() {
            if (this.owner != null) {
                this.mimic.lookAt(this.owner, 40.0F, 40.0F);
            }
            ((MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYRot(), true);
            this.mimic.getLookControl().setLookAt(this.owner, 40.0F, (float) this.mimic.getMaxHeadXRot());
            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = this.adjustedTickDelay(10);
                if (!this.mimic.isPassenger()) {
                    if (this.mimic.distanceToSqr(this.owner) >= 184.0D) {
                        this.tryTeleport();
                    } else {
                        ((MimicMoveControl) this.mimic.getMoveControl()).move(moveSpeed);
                    }
                }
            }
        }

        private void tryTeleport() {
            BlockPos blockPos = this.owner.blockPosition();

            for (int i = 0; i < 10; ++i) {
                int j = this.getRandomInt(-3, 3);
                int k = this.getRandomInt(-1, 1);
                int l = this.getRandomInt(-3, 3);
                boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (bl) {
                    return;
                }
            }

        }

        private boolean tryTeleportTo(int x, int y, int z) {
            if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.mimic.moveTo((double) x + 0.5D, (double) y, (double) z + 0.5D, this.mimic.getYRot(), this.mimic.getXRot());
                this.navigation.stop();
                return true;
            }
        }

        private boolean canTeleportTo(BlockPos pos) {
            PathType pathNodeType = WalkNodeEvaluator.getPathTypeStatic(this.mimic, pos.mutable());
            if (pathNodeType != PathType.WALKABLE) {
                return false;
            } else {
                BlockState blockState = this.world.getBlockState(pos.below());
                if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockPos = pos.subtract(this.mimic.blockPosition());
                    return this.world.noCollision(this.mimic, this.mimic.getBoundingBox().move(blockPos));
                }
            }
        }

        private int getRandomInt(int min, int max) {
            return this.mimic.getRandom().nextInt(max - min + 1) + min;
        }
    }

    public enum MimicState {
        SLEEPING,
        FLYING,
        IDLE,
        JUMPING,
        BITING,
        LANDING;

        public static final StreamCodec<ByteBuf, MimicState> NETWORK_CODEC = ByteBufCodecs.STRING_UTF8.map(MimicState::valueOf, Enum::name);
        public static final Codec<MimicState> CODEC = Codec.STRING.xmap(MimicState::valueOf, Enum::name);
    }
}
