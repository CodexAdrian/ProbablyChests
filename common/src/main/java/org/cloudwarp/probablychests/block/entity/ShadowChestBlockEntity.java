package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class ShadowChestBlockEntity extends PCBaseChestBlockEntity {

	public ShadowChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.SHADOW, pos, state);
	}
}
