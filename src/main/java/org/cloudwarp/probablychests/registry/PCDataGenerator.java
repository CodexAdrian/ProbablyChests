package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.cloudwarp.probablychests.data.PCWorldGenerator;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class PCDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(PCWorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.PLACED_FEATURE, PCFeatures::bootstrapPlaced);
		registryBuilder.add(Registries.CONFIGURED_FEATURE, PCFeatures::bootstrapConfigured);
	}
}
