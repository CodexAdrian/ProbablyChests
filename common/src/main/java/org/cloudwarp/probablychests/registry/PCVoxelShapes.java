package org.cloudwarp.probablychests.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PCVoxelShapes {
	public static final VoxelShape POT_VOXELSHAPE = Shapes.join(Block.box(3, 12, 3, 13, 16, 13), Block.box(1, 0, 1, 15, 12, 15), BooleanOp.OR);
}
