package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class UndergroundChestGeneration {
	/*public static void generateChest () {
		PCConfig config = ProbablyChests.loadedConfig;
		float chestRarity = config.worldGen.chestSpawnChance;
		if (chestRarity > 0) {
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
					GenerationStep.Feature.UNDERGROUND_DECORATION, PCFeatures.UNDERGROUND_CHEST_PLACED.getKey().get());
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInTheNether()),
					GenerationStep.Feature.UNDERGROUND_DECORATION, PCFeatures.NETHER_CHEST_PLACED.getKey().get());
		}
	}*/
}
