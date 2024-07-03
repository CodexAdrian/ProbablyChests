package org.cloudwarp.probablychests.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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

public class PCChestMimic extends PCTameablePetWithInventory implements GeoEntity, Monster {
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

    public PCChestMimic(EntityType<? extends PCTameablePetWithInventory> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.moveControl = new MimicMoveControl(this);
        this.experiencePoints = 10;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        MimicDifficulty mimicDifficulty = ProbablyChests.loadedConfig.mimicSettings.mimicDifficulty;
        return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, mimicDifficulty.getDamage())
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, mimicDifficulty.getHealth())
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
    }


    protected void initGoals() {
        this.goalSelector.add(7, new PCChestMimic.IdleGoal(this));
        this.goalSelector.add(5, new PCMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new PCChestMimic.SleepGoal(this));
        this.goalSelector.add(1, new PCChestMimic.SwimmingGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
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

    protected void jump() {
        Vec3d vec3d = this.getVelocity();
        LivingEntity livingEntity = this.getTarget();
        double jumpStrength;
        if (livingEntity == null) {
            jumpStrength = 1D;
        } else {
            jumpStrength = livingEntity.getY() - this.getY();
            jumpStrength = jumpStrength <= 0 ? 1.0D : Math.min(jumpStrength / 3.5D + 1.0D, 2.5D);
        }
        this.setVelocity(vec3d.x, (double) this.getJumpVelocity() * jumpStrength, vec3d.z);
        this.velocityDirty = true;
        if (this.isOnGround() && this.jumpEndTimer <= 0) {
            this.jumpEndTimer = 10;
            this.setMimicState(MimicState.JUMPING);
        }
    }

    protected boolean canAttack() {
        return this.canMoveVoluntarily();
    }

    protected float getDamageAmount() {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }


    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (float) ((int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
        if (bl) {
            this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
            this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
            this.applyDamageEffects(this, target);
        }

        return bl;
    }

    public double squaredDistanceToEntity(LivingEntity entity) {
        Vec3d vector = entity.getPos();
        double d = this.getX() - vector.x;
        double e = this.getY() - (vector.y + 0.6D);
        double f = this.getZ() - vector.z;
        return d * d + e * e + f * f;
    }

    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            return;
        }
        if (jumpEndTimer >= 0) {
            jumpEndTimer -= 1;
        }
        if (spawnWaitTimer > 0) {
            spawnWaitTimer -= 1;
        } else {
            if (this.isOnGround()) {
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
        this.onGroundLastTick = this.isOnGround();
    }

    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("wasOnGround", this.onGroundLastTick);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.onGroundLastTick = nbt.getBoolean("wasOnGround");
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(MIMIC_STATE, MimicState.SLEEPING);
        super.initDataTracker(builder);
    }

    public boolean cannotDespawn() {
        return this.hasVehicle();
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return true;
    }

    public static boolean isSpawnDark(ServerWorldAccess world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = world.getDimension();
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (i < 15 && world.getLightLevel(LightType.BLOCK, pos) > i) {
                return false;
            } else {
                PCConfig config = ProbablyChests.loadedConfig;
                int j = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
                return j <= dimensionType.monsterSpawnLightTest().get(random) * config.mimicSettings.naturalMimicSpawnRate;
            }
        }
    }

    public static boolean canSpawn(EntityType<PCChestMimic> pcChestMimicEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, net.minecraft.util.math.random.Random random) {
        return isSpawnDark(serverWorldAccess, blockPos, random);
    }

    @Override
    public EntityView method_48926() {
        return null;
    }
}
