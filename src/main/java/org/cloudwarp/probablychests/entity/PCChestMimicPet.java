package org.cloudwarp.probablychests.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.entity.ai.MimicMoveControl;
import org.cloudwarp.probablychests.entity.ai.PCMeleeAttackGoal;
import org.cloudwarp.probablychests.entity.ai.PCMimicEscapeDangerGoal;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class PCChestMimicPet extends PCTameablePetWithInventory implements GeoEntity, OwnableEntity {
    // Animations
    public static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
    public static final RawAnimation JUMP = RawAnimation.begin().then("jump", Animation.LoopType.PLAY_ONCE).then("flying", Animation.LoopType.LOOP);
    public static final RawAnimation CLOSE_SITTING = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE).then("sleeping", Animation.LoopType.LOOP);
    public static final RawAnimation CLOSE_STANDING = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE).then("standing", Animation.LoopType.LOOP);
    public static final RawAnimation OPENING = RawAnimation.begin().then("open", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation OPENED = RawAnimation.begin().then("opened", Animation.LoopType.LOOP);
    public static final RawAnimation SITTING = RawAnimation.begin().then("sleeping", Animation.LoopType.LOOP);
    public static final RawAnimation STANDING = RawAnimation.begin().then("standing", Animation.LoopType.LOOP);
    public static final RawAnimation FLYING = RawAnimation.begin().then("flying", Animation.LoopType.LOOP);
    public static final RawAnimation BITING = RawAnimation.begin().then("bite", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation LOW_WAG = RawAnimation.begin().then("lowWag", Animation.LoopType.LOOP);
    public static final RawAnimation FLYING_WAG = RawAnimation.begin().then("flyingWag", Animation.LoopType.LOOP);
    public static final RawAnimation IDLE_WAG = RawAnimation.begin().then("idleWag", Animation.LoopType.LOOP);
    public static final RawAnimation NO_WAG = RawAnimation.begin().then("noWag", Animation.LoopType.LOOP);
    private static final String MIMIC_CONTROLLER = "mimicController";
    private static final String TONGUE_CONTROLLER = "tongueController";


    public SimpleContainer inventory = new SimpleContainer(54);
    PCChestTypes type;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean onGroundLastTick;
    private int jumpEndTimer = 10;
    private int spawnWaitTimer = 5;


    public PCChestMimicPet(EntityType<? extends PCTameablePetWithInventory> entityType, Level world) {
        super(entityType, world);
        this.type = PCChestTypes.NORMAL;
        this.noCulling = true;
        this.inventory.addListener(this);
        this.moveControl = new MimicMoveControl(this);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PCTameablePetWithInventory.SwimmingGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        //this.goalSelector.add(1, new PetMimicEscapeDangerGoal(1.5));
        this.goalSelector.addGoal(5, new PCMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1, 5, 2, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
    }

    @Override
    public EntityGetter level() {
        return null;
    }

    class PetMimicEscapeDangerGoal
            extends PCMimicEscapeDangerGoal {
        public PetMimicEscapeDangerGoal(double speed) {
            super(PCChestMimicPet.this, speed);
        }

        @Override
        protected boolean isInDanger() {
            return this.mob.isFreezing() || this.mob.isOnFire();
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && stack.is(ItemTags.WOLF_FOOD);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    private <E extends GeoAnimatable> PlayState chestMovement(AnimationState<E> eAnimationState) {
        MimicState state = this.getMimicState();
        eAnimationState.getController().setAnimationSpeed(1D);
        eAnimationState.getController().transitionLength(6);
        eAnimationState.getController().setOverrideEasingType(EasingType.EASE_IN_OUT_SINE);
        switch (state) {
            case FLYING -> {
                eAnimationState.getController().transitionLength(2);
                eAnimationState.getController().setAnimation(FLYING);
            }
            case IDLE -> {
                if (this.getIsOpenState()) {
                    eAnimationState.getController().setAnimation(OPENED);
                } else {
                    if (this.isInSittingPose()) {
                        eAnimationState.getController().setAnimation(SITTING);
                    } else {
                        eAnimationState.getController().setAnimation(STANDING);
                    }
                }
            }
            case JUMPING -> {
                eAnimationState.getController().setAnimationSpeed(2D);
                eAnimationState.getController().setAnimation(JUMP);
            }
            case BITING -> {
                eAnimationState.getController().transitionLength(2);
                eAnimationState.getController().setAnimationSpeed(1.5D);
                eAnimationState.getController().setAnimation(BITING);
            }
            default -> System.out.println("INVALID STATE: " + state);
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState tongueMovement(AnimationState<E> eAnimationState) {
        MimicState state = this.getMimicState();
        eAnimationState.getController().setAnimationSpeed(1D);
        eAnimationState.getController().transitionLength(6);
        switch (state) {
            case IDLE -> {
                if (this.getIsOpenState()) {
                    eAnimationState.getController().setAnimation(IDLE_WAG);
                } else {
                    if (this.isInSittingPose()) {
                        eAnimationState.getController().setAnimation(NO_WAG);
                    } else {
                        eAnimationState.getController().setAnimation(LOW_WAG);
                    }
                }
            }
            case JUMPING -> {
                eAnimationState.getController().setAnimationSpeed(2D);
                eAnimationState.getController().setAnimation(FLYING_WAG);
            }
            case FLYING -> {
                eAnimationState.getController().transitionLength(2);
                eAnimationState.getController().setAnimation(FLYING_WAG);
            }
            default -> System.out.println("INVALID STATE: " + state);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, MIMIC_CONTROLLER, 6, this::chestMovement));
        controllerRegistrar.add(new AnimationController<>(this, TONGUE_CONTROLLER, 6, this::tongueMovement));
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
            jumpStrength = jumpStrength <= 0 ? 1D : jumpStrength / 3.5D + 1.0D;
        }
        //moveSpeed = this.world.random.nextDouble(1.5D,2.1D);
        this.setDeltaMovement(vec3d.x, (double) this.getJumpPower() * jumpStrength, vec3d.z);
        this.hasImpulse = true;
        if (this.onGround() && this.jumpEndTimer <= 0) {
            this.jumpEndTimer = 10;
            this.setMimicState(MimicState.JUMPING);
        }
    }


    public int getTicksUntilNextJump() {
        return 10;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        if (jumpEndTimer >= 0) {
            jumpEndTimer -= 1;
        }
        if (biteAnimationTimer > 0) {
            biteAnimationTimer -= 1;
        }
        if (this.onGround()) {
            if (!this.onGroundLastTick) {
                this.playSound(this.getLandingSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }
            if (biteAnimationTimer <= 0) {
                this.setMimicState(MimicState.IDLE);
            }
        } else {
            if (this.getMimicState() != MimicState.JUMPING) {
                if (spawnWaitTimer > 0) {
                    spawnWaitTimer -= 1;
                } else {
                    this.setMimicState(MimicState.FLYING);
                }
            }
        }
        this.onGroundLastTick = this.onGround();
        if (this.getIsAbandoned()) {
            this.setMimicState(MimicState.IDLE);
        }
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("wasOnGround", this.onGroundLastTick);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.onGroundLastTick = nbt.getBoolean("wasOnGround");
    }


    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MIMIC_STATE, MimicState.IDLE);
        super.defineSynchedData(builder);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

	/*public static class MimicMoveControl extends MoveControl {
		private final PCChestMimicPet mimic;
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public MimicMoveControl (PCChestMimicPet mimic) {
			super(mimic);
			this.mimic = mimic;
			this.targetYaw = 180.0F * mimic.getYaw() / 3.1415927F;
		}

		public void look (float targetYaw, boolean jumpOften) {
			this.targetYaw = targetYaw;
			this.jumpOften = jumpOften;
		}

		public void move (double speed) {
			this.speed = speed;
			this.state = State.MOVE_TO;
		}

		public void tick () {
			this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
			this.entity.headYaw = this.entity.getYaw();
			this.entity.bodyYaw = this.entity.getYaw();
			if (this.state != State.MOVE_TO) {
				this.entity.setForwardSpeed(0.0F);
			} else {
				this.state = State.WAIT;
				if (this.entity.isOnGround()) {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
					if (this.ticksUntilJump-- <= 0) {
						this.ticksUntilJump = this.mimic.getTicksUntilNextJump();
						if (this.jumpOften) {
							this.ticksUntilJump /= 3;
						}

						this.mimic.getJumpControl().setActive();
						this.mimic.playSound(this.mimic.getJumpSound(), this.mimic.getSoundVolume(), this.mimic.getJumpSoundPitch());
					} else {
						this.mimic.sidewaysSpeed = 0.0F;
						this.mimic.forwardSpeed = 0.0F;
						this.entity.setMovementSpeed(0.0F);
					}
				} else {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				}

			}
		}
	}*/

    static class SitGoal extends Goal {
        private final PCChestMimicPet mimic;

        public SitGoal(PCChestMimicPet mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canContinueToUse() {
            return this.mimic.isOrderedToSit();
        }

        public boolean canUse() {
            if (!this.mimic.isTame()) {
                return false;
            } else if (this.mimic.isInWaterOrBubble()) {
                return false;
            } else if (!this.mimic.onGround()) {
                return false;
            } else {
                LivingEntity livingEntity = this.mimic.getOwner();
                if (livingEntity == null) {
                    return true;
                } else {
                    return (!(this.mimic.distanceToSqr(livingEntity) < 144.0D) || livingEntity.getLastHurtByMob() == null) && this.mimic.isOrderedToSit();
                }
            }
        }

        public void start() {
            this.mimic.getNavigation().stop();
            this.mimic.setInSittingPose(true);
        }

        public void stop() {
            this.mimic.setInSittingPose(false);
        }
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


    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Ghast) || this.getIsAbandoned()) {
            if (target instanceof PCChestMimicPet) {
                PCChestMimicPet mimic = (PCChestMimicPet) target;
                return !mimic.isTame() || mimic.getOwner() != owner;
            } else if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
                return false;
            } else {
                return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
            }
        } else {
            return false;
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
                this.setMimicState(MimicState.IDLE);
            }

            if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.hurt(source, amount);
        }
    }

}
