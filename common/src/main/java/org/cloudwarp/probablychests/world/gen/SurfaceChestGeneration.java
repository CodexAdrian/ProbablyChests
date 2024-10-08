package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;
import java.util.function.Predicate;

public class SurfaceChestGeneration {
	public static void generateChest () {
		PCConfig config = ProbablyChests.loadedConfig;
		float surfaceChestRarity = config.worldGen.surfaceChestSpawnChance;
		if (surfaceChestRarity > 0) {
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().or(BiomeSelectors.foundInTheEnd()), GenerationStep.Decoration.VEGETAL_DECORATION, PCFeatures.SURFACE_CHEST_PLACED_KEY);
		}
	}
}
