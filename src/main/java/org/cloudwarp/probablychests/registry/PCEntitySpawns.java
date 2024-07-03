package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;

public class PCEntitySpawns {

	public static void init(){
		PCConfig config = ProbablyChests.loadedConfig;
		if(config.mimicSettings.spawnNaturalMimics) {
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), MobCategory.MONSTER, PCEntities.NORMAL_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), MobCategory.MONSTER, PCEntities.ROCKY_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), MobCategory.MONSTER, PCEntities.LUSH_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), MobCategory.MONSTER, PCEntities.GOLD_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), MobCategory.MONSTER, PCEntities.STONE_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIFIED_PIGLIN), MobCategory.MONSTER, PCEntities.NETHER_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIFIED_PIGLIN), MobCategory.MONSTER, PCEntities.SHADOW_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.GOAT), MobCategory.MONSTER, PCEntities.ICE_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.DROWNED), MobCategory.MONSTER, PCEntities.CORAL_CHEST_MIMIC, 1, 1, 1);
		}
	}
}
