package org.cloudwarp.probablychests.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.cloudwarp.probablychests.utils.VoxelShaper;

import java.util.Map;

public class PCPot extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final MapCodec<PCPot> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            createSettingsCodec(),
            RecordCodecBuilder.point(VoxelShapes.empty())
    ).apply(instance, PCPot::new));

    private final Map<Direction, VoxelShape> shapes;

    public PCPot(Settings settings, VoxelShape voxelShape) {
        super(settings.pistonBehavior(PistonBehavior.DESTROY));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
        shapes = VoxelShaper.generateRotations(voxelShape);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return shapes.get(direction);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return TorchBlock.sideCoversSmallSquare(world, pos.down(), Direction.UP);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
}
