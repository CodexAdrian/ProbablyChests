package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class RockyChestBlockEntity extends PCBaseChestBlockEntity {

	public RockyChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.ROCKY, pos, state);
	}
}
