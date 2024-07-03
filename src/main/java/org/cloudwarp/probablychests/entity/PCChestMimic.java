package org.cloudwarp.probablychests.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.ai.MimicMoveControl;
import org.cloudwarp.probablychests.entity.ai.PCMeleeAttackGoal;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.utils.MimicDifficulty;
import org.cloudwarp.probablychests.utils.PCConfig;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PCChestMimic extends PCTameablePetWithInventory implements GeoEntity, Enemy {
    // Animations
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation JUMP = RawAnimation.begin().thenPlay("jump").thenLoop("flying");
    public static final RawAnimation CLOSE = RawAnimation.begin().thenPlay("land").thenLoop("idle");
    public static final RawAnimation SLEEPING = RawAnimation.begin().thenLoop("sleeping");
    public static final RawAnimation FLYING = RawAnimation.begin().thenLoop("flying");
    public static final RawAnimation FLYING_WAG = RawAnimation.begin().thenLoop("flyingWag");
    public static final RawAnimation IDLE_WAG = RawAnimation.begin().thenLoop("idleWag");
    public static final RawAnimation NO_WAG = RawAnimation.begin().thenLoop("noWag");
    private static final String MIMIC_CONTROLLER = "mimicController";
    private static final String TONGUE_CONTROLLER = "tongueController";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean onGroundLastTick;
    private int timeUntilSleep = 0;
    private int jumpEndTimer = 10;
    private int spawnWaitTimer = 10;
    private boolean isAttemptingToSleep = true;

    public PCChestMimic(EntityType<? extends PCTameablePetWithInventory> entityType, Level world) {
        super(entityType, world);
        this.noCulling = true;
        this.moveControl = new MimicMoveControl(this);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        MimicDifficulty mimicDifficulty = ProbablyChests.loadedConfig.mimicSettings.mimicDifficulty;
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.ATTACK_DAMAGE, mimicDifficulty.getDamage())
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.MAX_HEALTH, mimicDifficulty.getHealth())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(7, new PCChestMimic.IdleGoal(this));
        this.goalSelector.addGoal(5, new PCMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new PCChestMimic.SleepGoal(this));
        this.goalSelector.addGoal(1, new PCChestMimic.SwimmingGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
    }

    private <E extends GeoAnimatable> PlayState chestMovement(AnimationState<E> eAnimationState) {
        MimicState state = this.getMimicState();
        eAnimationState.getController().setAnimationSpeed(1D);
        return switch (state) {
            case FLYING -> eAnimationState.setAndContinue(FLYING);
            case LANDING -> eAnimationState.setAndContinue(CLOSE);
            case IDLE -> eAnimationState.setAndContinue(IDLE);
            case JUMPING -> {
                eAnimationState.getController().setAnimationSpeed(2D);
                yield eAnimationState.setAndContinue(JUMP);
            }
            default -> eAnimationState.setAndContinue(SLEEPING);
        };
    }

    private <E extends GeoAnimatable> PlayState tongueMovement(AnimationState<E> eAnimationState) {
        MimicState state = this.getMimicState();
        eAnimationState.getController().setAnimationSpeed(1D);
        //eAnimationState.getController().transitionLengthTicks = 1;
        return switch (state) {
            case FLYING -> eAnimationState.setAndContinue(FLYING_WAG);
            case IDLE -> {
                eAnimationState.getController().setAnimationSpeed(1.5D);
                yield eAnimationState.setAndContinue(IDLE_WAG);
            }
            case JUMPING -> {
                eAnimationState.getController().setAnimationSpeed(2D);
                yield eAnimationState.setAndContinue(FLYING_WAG);
            }
            default -> eAnimationState.setAndContinue(NO_WAG);
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, MIMIC_CONTROLLER, 6, this::chestMovement));
        controllers.add(new AnimationController<>(this, TONGUE_CONTROLLER, 6, this::tongueMovement));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected void jumpFromGround() {
        Vec3 vec3d = this.getDeltaMovement();
        LivingEntity livingEntity = this.getTarget();
        double jumpStrength;
        if (livingEntity == null) {
            jumpStrength = 1D;
        } else {
            jumpStrength = livingEntity.getY() - this.getY();
            jumpStrength = jumpStrength <= 0 ? 1.0D : Math.min(jumpStrength / 3.5D + 1.0D, 2.5D);
        }
        this.setDeltaMovement(vec3d.x, (double) this.getJumpPower() * jumpStrength, vec3d.z);
        this.hasImpulse = true;
        if (this.onGround() && this.jumpEndTimer <= 0) {
            this.jumpEndTimer = 10;
            this.setMimicState(MimicState.JUMPING);
        }
    }

    protected boolean canAttack() {
        return this.isEffectiveAi();
    }

    protected float getDamageAmount() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }


    public boolean doHurtTarget(Entity target) {
        boolean bl = target.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (bl) {
            this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
            this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
            this.doEnchantDamageEffects(this, target);
        }

        return bl;
    }

    public double squaredDistanceToEntity(LivingEntity entity) {
        Vec3 vector = entity.position();
        double d = this.getX() - vector.x;
        double e = this.getY() - (vector.y + 0.6D);
        double f = this.getZ() - vector.z;
        return d * d + e * e + f * f;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        if (jumpEndTimer >= 0) {
            jumpEndTimer -= 1;
        }
        if (spawnWaitTimer > 0) {
            spawnWaitTimer -= 1;
        } else {
            if (this.onGround()) {
                if (this.onGroundLastTick) {
                    if (this.getMimicState() != MimicState.SLEEPING && !isAttemptingToSleep) {
                        timeUntilSleep = 150;
                        isAttemptingToSleep = true;
                        this.setMimicState(MimicState.IDLE);
                    }
                    if (isAttemptingToSleep) {
                        timeUntilSleep -= 1;
                        if (timeUntilSleep <= 0) {
                            timeUntilSleep = 0;
                            this.setMimicState(MimicState.SLEEPING);
                        }
                    }
                } else {
                    isAttemptingToSleep = false;
                    this.setMimicState(MimicState.LANDING);
                    this.playSound(this.getLandingSound(), this.getSoundVolume(),
                            ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
                }
            } else {
                isAttemptingToSleep = false;
                if (this.getMimicState() != MimicState.JUMPING) {
                    this.setMimicState(MimicState.FLYING);
                }
            }
        }
        this.onGroundLastTick = this.onGround();
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("wasOnGround", this.onGroundLastTick);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.onGroundLastTick = nbt.getBoolean("wasOnGround");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MIMIC_STATE, MimicState.SLEEPING);
        super.defineSynchedData(builder);
    }

    public boolean requiresCustomPersistence() {
        return this.isPassenger();
    }

    public boolean removeWhenFarAway(double distanceSquared) {
        return true;
    }

    public static boolean isSpawnDark(ServerLevelAccessor world, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = world.dimensionType();
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (i < 15 && world.getBrightness(LightLayer.BLOCK, pos) > i) {
                return false;
            } else {
                PCConfig config = ProbablyChests.loadedConfig;
                int j = world.getLevel().isThundering() ? world.getMaxLocalRawBrightness(pos, 10) : world.getMaxLocalRawBrightness(pos);
                return j <= dimensionType.monsterSpawnLightTest().sample(random) * config.mimicSettings.naturalMimicSpawnRate;
            }
        }
    }

    public static boolean canSpawn(EntityType<PCChestMimic> pcChestMimicEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, net.minecraft.util.RandomSource random) {
        return isSpawnDark(serverWorldAccess, blockPos, random);
    }

    @Override
    public EntityGetter level() {
        return null;
    }
}
