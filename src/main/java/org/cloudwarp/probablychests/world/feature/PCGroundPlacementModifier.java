package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PCGroundPlacementModifier extends PlacementModifier {


	public static final MapCodec<PCGroundPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			(Direction.VERTICAL_CODEC.fieldOf("direction_of_search")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.direction),
			(BlockPredicate.CODEC.fieldOf("direction_target_condition")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.directionPredicate),
			(BlockPredicate.CODEC.fieldOf("target_condition")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.targetPredicate),
			(Codec.intRange(1, 32).fieldOf("max_steps")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.maxSteps),
			Heightmap.Types.CODEC.fieldOf("heightmap").forGetter((PCGroundPlacementModifier) -> PCGroundPlacementModifier.heightmap),
			(Codec.INT.fieldOf("max_height").forGetter((PCGroundPlacementModifier) -> PCGroundPlacementModifier.maxHeight))
	).apply(instance, PCGroundPlacementModifier::new));
	//---------------------------------------------------
	private final Direction direction;
	private final BlockPredicate directionPredicate;
	private final BlockPredicate targetPredicate;
	private final int maxSteps;
	private final Heightmap.Types heightmap;
	private final int maxHeight;


	private PCGroundPlacementModifier (Direction direction, BlockPredicate directionPredicate, BlockPredicate targetPredicate, int maxSteps, Heightmap.Types heightmap, int maxHeight) {
		this.direction = direction;
		this.targetPredicate = targetPredicate;
		this.directionPredicate = directionPredicate;
		this.maxSteps = maxSteps;
		this.heightmap = heightmap;
		this.maxHeight = maxHeight;
	}

	public static PCGroundPlacementModifier of (Direction direction, BlockPredicate directionPredicate, BlockPredicate targetPredicate, int maxSteps, Heightmap.Types heightmap, int maxHeight) {
		return new PCGroundPlacementModifier(direction, directionPredicate, targetPredicate, maxSteps, heightmap, maxHeight);
	}

	@Override
	public Stream<BlockPos> count (PlacementContext context, RandomSource random, BlockPos pos) {
		BlockPos.MutableBlockPos mutableTarget = pos.mutable();
		// get top of heightmap
		int k = context.getHeight(this.heightmap, mutableTarget.getX(), mutableTarget.getZ());
		// get which is lower
		k = Math.min(this.maxHeight, k);
		if (context.getMinBuildHeight() >= k - 1) {
			return Stream.of(new BlockPos[0]);
		}
		//k - (1 + random.nextInt(Math.abs(context.getBottomY() - k)))
		mutableTarget.set(mutableTarget.getX(), random.nextIntBetweenInclusive(context.getMinBuildHeight(), k - 1), mutableTarget.getZ());
		BlockPos.MutableBlockPos mutableDirection = mutableTarget.mutable();
		mutableDirection.move(Direction.DOWN);
		WorldGenLevel structureWorldAccess = context.getLevel();


		// TODO: change IS AIR to REPLACABLE.
		for (int i = 0; i < this.maxSteps; ++ i) {
			if (this.targetPredicate.test(structureWorldAccess, mutableTarget) &&
					this.directionPredicate.test(structureWorldAccess, mutableDirection) &&
					! BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.BEDROCK).test(structureWorldAccess, mutableDirection)) {
				return Stream.of(mutableTarget);
			}
			mutableTarget.move(this.direction);
			mutableDirection.move(this.direction);
			if (structureWorldAccess.isOutsideBuildHeight(mutableTarget.getY())) {
				return Stream.of(new BlockPos[0]);
			}
		}
		if (this.targetPredicate.test(structureWorldAccess, mutableTarget) &&
				this.directionPredicate.test(structureWorldAccess, mutableDirection) &&
				! BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.BEDROCK).test(structureWorldAccess, mutableDirection)) {
			return Stream.of(mutableTarget);
		}
		return Stream.of(new BlockPos[0]);
	}

	@Override
	public PlacementModifierType<?> type () {
		return PCPlacementModifierType.CHEST_SCAN;
	}
}
