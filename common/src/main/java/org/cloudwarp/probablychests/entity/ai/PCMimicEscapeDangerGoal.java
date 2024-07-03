package org.cloudwarp.probablychests.entity.ai;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class PCMimicEscapeDangerGoal extends Goal {
	public static final int field_36271 = 1;
	protected final PathfinderMob mob;
	protected final double speed;
	protected double targetX;
	protected double targetY;
	protected double targetZ;
	protected boolean active;

	public PCMimicEscapeDangerGoal(PathfinderMob mob, double speed) {
		this.mob = mob;
		this.speed = speed;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.JUMP, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		BlockPos blockPos;
		if (!this.isInDanger()) {
			return false;
		}
		if (this.mob.isOnFire() && (blockPos = this.locateClosestWater(this.mob.level(), this.mob, 5)) != null) {
			this.targetX = blockPos.getX();
			this.targetY = blockPos.getY();
			this.targetZ = blockPos.getZ();
			return true;
		}
		return this.findTarget();
	}

	protected boolean isInDanger() {
		return this.mob.getLastHurtByMob() != null || this.mob.isFreezing() || this.mob.isOnFire();
	}

	protected boolean findTarget() {
		Vec3 vec3d = DefaultRandomPos.getPos(this.mob, 5, 4);
		if (vec3d == null) {
			return false;
		}
		this.targetX = vec3d.x;
		this.targetY = vec3d.y;
		this.targetZ = vec3d.z;
		return true;
	}

	public boolean isActive() {
		return this.active;
	}

	@Override
	public void start() {
		this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
		this.active = true;
	}

	@Override
	public void stop() {
		this.active = false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.mob.getNavigation().isDone();
	}

	@Nullable
	protected BlockPos locateClosestWater(BlockGetter world, Entity entity, int rangeX) {
		BlockPos blockPos = entity.blockPosition();
		if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()) {
			return null;
		}
		return BlockPos.findClosestMatch(entity.blockPosition(), rangeX, 1, pos -> world.getFluidState((BlockPos)pos).is(FluidTags.WATER)).orElse(null);
	}
}

