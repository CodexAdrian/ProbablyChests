package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class NormalChestBlockEntity extends PCBaseChestBlockEntity {

	public NormalChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.NORMAL, pos, state);
	}
}
