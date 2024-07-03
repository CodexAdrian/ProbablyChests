package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class CoralChestBlockEntity extends PCBaseChestBlockEntity {

	public CoralChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.CORAL, pos, state);
	}
}
