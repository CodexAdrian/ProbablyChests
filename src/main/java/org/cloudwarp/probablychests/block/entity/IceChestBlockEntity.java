package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class IceChestBlockEntity extends PCBaseChestBlockEntity {

	public IceChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.ICE, pos, state);
	}
}
