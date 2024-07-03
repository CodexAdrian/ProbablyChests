package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PCRarityFilterPlacementModifier extends PlacementFilter {
	public static final MapCodec<PCRarityFilterPlacementModifier> MODIFIER_CODEC = ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").xmap(PCRarityFilterPlacementModifier::new, (PCRarityFilterPlacementModifier) -> {
		return PCRarityFilterPlacementModifier.chance;
	});

	private final float chance;

	private PCRarityFilterPlacementModifier (float chance) {
		this.chance = chance;
	}

	public static PCRarityFilterPlacementModifier of (float chance) {
		return new PCRarityFilterPlacementModifier(chance);
	}

	protected boolean shouldPlace (PlacementContext context, RandomSource random, BlockPos pos) {
		return random.nextFloat() < this.chance;
	}

	public PlacementModifierType<?> type () {
		return PCPlacementModifierType.PC_RARITY;
	}
}
