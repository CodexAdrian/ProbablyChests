package org.cloudwarp.probablychests;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.probablychests.network.PCCommonNetworking;
import org.cloudwarp.probablychests.registry.*;
import org.cloudwarp.probablychests.utils.MimicDifficulty;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCEventHandler;
import org.cloudwarp.probablychests.world.feature.PCPlacementModifierType;
import org.cloudwarp.probablychests.world.gen.PCWorldGen;

public class ProbablyChests {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "probablychests";
	public static ConfigHolder<PCConfig> configHolder;
	public static PCConfig loadedConfig;

	public static ResourceLocation id (String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public void onInitialize () {
		LOGGER.info("[Probably-Chests] is initializing.");
		AutoConfig.register(PCConfig.class, Toml4jConfigSerializer::new);
		configHolder = AutoConfig.getConfigHolder(PCConfig.class);
		loadedConfig = getConfig();
		PCEventHandler.registerEvents();
		PCItemGroups.init();
		PCSounds.init();
		PCStatistics.init();
		PCLootTables.init();
		PCBlockEntities.init();
		PCBlocks.init();
		PCEntities.init();
		PCItems.init();
		PCPlacementModifierType.init();
		//PCFeatures.init();
		PCEntitySpawns.init();
		PCScreenHandlerType.registerScreenHandlers();
		PCWorldGen.generatePCWorldGen();
		PCCommonNetworking.init();
		PCRegisteredFeatures.init();
		LOGGER.info("[Probably-Chests] has successfully been initialized.");
		LOGGER.info("[Probably-Chests] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}

	public static PCConfig getConfig () {
		return configHolder.getConfig();
	}

	public static CompoundTag configToNBT(){
		PCConfig config = getConfig();
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("pot_spawn_chance", config.worldGen.potSpawnChance);
		nbt.putFloat("chest_spawn_chance", config.worldGen.chestSpawnChance);
		nbt.putFloat("surface_chest_spawn_chance", config.worldGen.surfaceChestSpawnChance);
		nbt.putFloat("secret_mimic_chance", config.worldGen.secretMimicChance);
		nbt.putInt("mimic_difficulty", config.mimicSettings.mimicDifficulty.toInt());
		nbt.putBoolean("spawn_natural_mimics", config.mimicSettings.spawnNaturalMimics);
		nbt.putFloat("natural_mimic_spawn_rate", config.mimicSettings.naturalMimicSpawnRate);
		nbt.putBoolean("allow_pet_mimics", config.mimicSettings.allowPetMimics);
		nbt.putBoolean("do_pet_mimic_limit", config.mimicSettings.doPetMimicLimit);
		nbt.putInt("pet_mimic_limit", config.mimicSettings.petMimicLimit);
		nbt.putInt("abandoned_mimic_timer", config.mimicSettings.abandonedMimicTimer);
		nbt.putBoolean("allow_pet_mimic_locking", config.mimicSettings.allowPetMimicLocking);
		nbt.putBoolean("allow_chest_locking", config.chestSettings.allowChestLocking);
		nbt.putBoolean("enable_locked_chest_owners", config.chestSettings.enableLockedChestOwners);
		return nbt;
	}
	public static PCConfig nbtToConfig(CompoundTag nbt){
		PCConfig config = new PCConfig();
		if(nbt == null){
			return config;
		}
		config.worldGen.potSpawnChance = nbt.getFloat("pot_spawn_chance");
		config.worldGen.chestSpawnChance = nbt.getFloat("chest_spawn_chance");
		config.worldGen.surfaceChestSpawnChance = nbt.getFloat("surface_chest_spawn_chance");
		config.worldGen.secretMimicChance = nbt.getFloat("secret_mimic_chance");
		config.mimicSettings.mimicDifficulty = MimicDifficulty.fromInt( nbt.getInt("mimic_difficulty"));
		config.mimicSettings.spawnNaturalMimics = nbt.getBoolean("spawn_natural_mimics");
		config.mimicSettings.naturalMimicSpawnRate = nbt.getFloat("natural_mimic_spawn_rate");
		config.mimicSettings.allowPetMimics = nbt.getBoolean("allow_pet_mimics");
		config.mimicSettings.doPetMimicLimit = nbt.getBoolean("do_pet_mimic_limit");
		config.mimicSettings.petMimicLimit = nbt.getInt("pet_mimic_limit");
		config.mimicSettings.abandonedMimicTimer = nbt.getInt("abandoned_mimic_timer");
		config.mimicSettings.allowPetMimicLocking = nbt.getBoolean("allow_pet_mimic_locking");
		config.chestSettings.allowChestLocking = nbt.getBoolean("allow_chest_locking");
		config.chestSettings.enableLockedChestOwners = nbt.getBoolean("enable_locked_chest_owners");
		return config;
	}
}
