package org.cloudwarp.probablychests.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class PCChestMimic extends PCTameablePetWithInventory implements IAnimatable, Monster {
	// Animations
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", true);
	public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("jump", false).addAnimation("flying", true);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("close", false).addAnimation("idle", true);
	public static final AnimationBuilder SLEEPING = new AnimationBuilder().addAnimation("sleeping", true);
	public static final AnimationBuilder FLYING = new AnimationBuilder().addAnimation("flying", true);
	private static final String CONTROLLER_NAME = "mimicController";

	private static final TrackedData<Integer> MIMIC_STATE;
	private static double moveSpeed = 1.5D;
	private static int maxHealth = 50;
	private static int maxDamage = 5;

	// Mimic States
	private static final int IS_SLEEPING = 0;
	private static final int IS_IN_AIR = 1;
	private static final int IS_CLOSED = 2;
	private static final int IS_IDLE = 3;
	private static final int IS_JUMPING = 4;


	static {
		MIMIC_STATE = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.INTEGER);
	}

	public SimpleInventory inventory = new SimpleInventory(54);
	private AnimationFactory factory = new AnimationFactory(this);
	private boolean onGroundLastTick;
	private int timeUntilSleep = 0;
	private int jumpEndTimer = 10;
	private int spawnWaitTimer = 5;

	public PCChestMimic (EntityType<? extends PCTameablePetWithInventory> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
		this.moveControl = new PCChestMimic.MimicMoveControl(this);
		this.experiencePoints = 10;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		boolean easyMimics = ProbablyChests.loadedConfig.mimicSettings.easierMimics;
		if (easyMimics) {
			maxHealth = 30;
			maxDamage = 3;
			moveSpeed = 1D;
		}
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, maxDamage)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, maxHealth)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
	}


	protected void initGoals () {
		this.goalSelector.add(2, new PCChestMimic.FaceTowardTargetGoal(this));
		this.goalSelector.add(7, new PCChestMimic.IdleGoal(this));
		this.goalSelector.add(1, new PCChestMimic.SwimmingGoal(this));
		this.goalSelector.add(5, new PCChestMimic.MoveGoal(this));
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
	}

	/*
	Mimic States:
	0 = sleeping
	1 = in air
	2 = on ground
	3 = idle
	4 = jumping
	 */
	private <E extends IAnimatable> PlayState devMovement (AnimationEvent<E> animationEvent) {
		int state = this.getMimicState();
		animationEvent.getController().setAnimationSpeed(1D);
		switch (state) {
			case IS_SLEEPING:
				animationEvent.getController().setAnimation(SLEEPING);
				break;
			case IS_IN_AIR:
				animationEvent.getController().setAnimation(FLYING);
				break;
			case IS_CLOSED:
				animationEvent.getController().setAnimation(CLOSE);
				break;
			case IS_IDLE:
				animationEvent.getController().setAnimation(IDLE);
				break;
			case IS_JUMPING:
				animationEvent.getController().setAnimationSpeed(2D);
				animationEvent.getController().setAnimation(JUMP);
				break;
			default:
				animationEvent.getController().setAnimation(SLEEPING);
				break;
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, CONTROLLER_NAME, 3, this::devMovement));
	}

	@Override
	public AnimationFactory getFactory () {
		return this.factory;
	}


	protected void jump () {
		Vec3d vec3d = this.getVelocity();
		LivingEntity livingEntity = this.getTarget();
		double jumpStrength;
		if (livingEntity == null) {
			jumpStrength = 1D;
		} else {
			jumpStrength = livingEntity.getY() - this.getY();
			jumpStrength = jumpStrength <= 0 ? 1D : Math.min(jumpStrength / 2.5D + 1.0D, 2.5D);
		}
		this.setVelocity(vec3d.x, (double) this.getJumpVelocity() * jumpStrength, vec3d.z);
		this.velocityDirty = true;
		if (this.isOnGround() && this.jumpEndTimer <= 0) {
			this.jumpEndTimer = 10;
			this.setMimicState(IS_JUMPING);
		}
	}

	protected boolean canAttack () {
		return true;
	}

	protected float getDamageAmount () {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
	}

	public void onPlayerCollision (PlayerEntity player) {
		if (this.canAttack()) {
			this.damage(player);
		}

	}

	protected void damage (LivingEntity target) {
		if (this.isAlive()) {
			if (this.squaredDistanceTo(target) < 1.5D && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
				this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
				this.applyDamageEffects(this, target);
			}
		}
	}

	protected int getTicksUntilNextJump () {
		return this.random.nextInt(40) + 5;
	}

	/*
	Mimic States:
	0 = sleeping
	1 = in air
	2 = on ground
	3 = idle
	4 = jumping
	 */
	public void tick () {
		super.tick();
		if (jumpEndTimer >= 0) {
			jumpEndTimer -= 1;
		}
		if (this.onGround) {
			if (this.onGroundLastTick) {
				if (! this.isSleeping() && this.getMimicState() != IS_IDLE) {
					timeUntilSleep = 150;
					this.setMimicState(IS_IDLE);
				}
				if (this.getMimicState() == IS_IDLE) {
					timeUntilSleep -= 1;
					if (timeUntilSleep <= 0) {
						this.setMimicState(IS_SLEEPING);
					}
				}
			} else {
				this.setMimicState(IS_CLOSED);
				this.playSound(this.getLandingSound(), this.getSoundVolume(),
						((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}
		} else {
			if (this.getMimicState() != IS_JUMPING) {
				if (spawnWaitTimer > 0) {
					spawnWaitTimer -= 1;
				} else {
					this.setMimicState(IS_IN_AIR);
				}
			}
		}

		this.onGroundLastTick = this.onGround;
	}

	protected boolean isDisallowedInPeaceful () {
		return true;
	}

	public void writeCustomDataToNbt (NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("wasOnGround", this.onGroundLastTick);
		nbt.putInt("state", this.getMimicState());
		NbtList listnbt = new NbtList();
		for (int i = 0; i < this.inventory.size(); ++ i) {
			ItemStack itemstack = this.inventory.getStack(i);
			NbtCompound compoundnbt = new NbtCompound();
			compoundnbt.putByte("Slot", (byte) i);
			itemstack.writeNbt(compoundnbt);
			listnbt.add(compoundnbt);

		}
		nbt.put("Inventory", listnbt);
	}

	public void readCustomDataFromNbt (NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.onGroundLastTick = nbt.getBoolean("wasOnGround");
		this.setMimicState(nbt.getInt("state"));
		NbtList listnbt = nbt.getList("Inventory", 10);
		for (int i = 0; i < listnbt.size(); ++ i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			this.inventory.setStack(j, ItemStack.fromNbt(compoundnbt));
		}
	}


	public void setMimicState (int state) {
		this.dataTracker.set(MIMIC_STATE, state);
	}

	public int getMimicState () {
		return this.dataTracker.get(MIMIC_STATE);
	}

	@Override
	protected void initDataTracker () {
		this.dataTracker.startTracking(MIMIC_STATE, 0);
		super.initDataTracker();
	}

	public boolean cannotDespawn () {
		return this.hasVehicle() || ! this.inventory.isEmpty();
	}

	public static boolean isSpawnDark (ServerWorldAccess world, BlockPos pos, Random random) {
		if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
			return false;
		} else if (world.getLightLevel(LightType.BLOCK, pos) > 0) {
			return false;
		} else {
			PCConfig config = ProbablyChests.loadedConfig;
			int i = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
			return i <= random.nextInt(8) * config.mimicSettings.naturalMimicSpawnRate;
		}
	}

	public static boolean canSpawn (EntityType<PCChestMimic> pcChestMimicEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
		if (serverWorldAccess.isSkyVisible(blockPos) || isSpawnDark(serverWorldAccess, blockPos, random)) {
			return false;
		}
		return true;
	}

	private static class MimicMoveControl extends MoveControl {
		private final PCChestMimic mimic;
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public MimicMoveControl (PCChestMimic mimic) {
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
	}

	static class FaceTowardTargetGoal extends Goal {
		private final PCChestMimic mimic;
		private int ticksLeft;

		public FaceTowardTargetGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.LOOK));
		}

		public boolean canStart () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else {
				return ! this.mimic.canTarget(livingEntity) ? false : this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
			}
		}

		public void start () {
			this.ticksLeft = toGoalTicks(300);
			super.start();
		}

		public boolean shouldContinue () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (! this.mimic.canTarget(livingEntity)) {
				return false;
			} else {
				return -- this.ticksLeft > 0;
			}
		}

		public boolean shouldRunEveryTick () {
			return true;
		}

		public void tick () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity != null) {
				this.mimic.lookAtEntity(livingEntity, 10.0F, 10.0F);
			}

			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYaw(), this.mimic.canAttack());
		}
	}

	static class MoveGoal extends Goal {
		private final PCChestMimic mimic;

		public MoveGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
		}

		public boolean canStart () {
			//return !this.mimc.hasVehicle();
			return this.mimic.getTarget() != null;
		}

		public void tick () {
			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).move(moveSpeed);
		}
	}

	static class SwimmingGoal extends Goal {
		private final PCChestMimic mimic;

		public SwimmingGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
			mimic.getNavigation().setCanSwim(true);
		}

		public boolean canStart () {
			return (this.mimic.isTouchingWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
		}

		public boolean shouldRunEveryTick () {
			return true;
		}

		public void tick () {
			if (this.mimic.getRandom().nextFloat() < 0.8F) {
				this.mimic.getJumpControl().setActive();
			}

			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).move(4.2D);
		}
	}

	static class IdleGoal extends Goal {
		private final PCChestMimic mimic;

		public IdleGoal (PCChestMimic mimic) {
			this.mimic = mimic;

		}

		public boolean canStart () {
			return ! this.mimic.hasVehicle();
		}

		public void tick () {

		}
	}

}
