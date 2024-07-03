package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PCSolidGroundPlacementModifier extends PlacementModifier {
	public static final MapCodec<PCSolidGroundPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			(BlockPredicate.CODEC.fieldOf("target_condition")).forGetter(PCSolidGroundPlacementModifier -> PCSolidGroundPlacementModifier.targetPredicate)
	).apply(instance, PCSolidGroundPlacementModifier::new));
	//---------------------------------------------------

	private final BlockPredicate targetPredicate;


	private PCSolidGroundPlacementModifier (BlockPredicate targetPredicate) {

		this.targetPredicate = targetPredicate;

	}

	public static PCSolidGroundPlacementModifier of (BlockPredicate targetPredicate) {
		return new PCSolidGroundPlacementModifier(targetPredicate);
	}

	@Override
	public Stream<BlockPos> getPositions (PlacementContext context, RandomSource random, BlockPos pos) {
		BlockPos.MutableBlockPos mutableTarget = pos.mutable();
		mutableTarget.move(Direction.DOWN);
		WorldGenLevel structureWorldAccess = context.getLevel();
		if (this.targetPredicate.test(structureWorldAccess, mutableTarget)) {
			return Stream.of(pos);
		}
		return Stream.of(new BlockPos[0]);
	}

	@Override
	public PlacementModifierType<?> type () {
		return PCPlacementModifierType.SOLID_CHECK;
	}
}
