package org.cloudwarp.probablychests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class StoneChestBlockEntity extends PCBaseChestBlockEntity {

	public StoneChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.STONE, pos, state);
	}
}
