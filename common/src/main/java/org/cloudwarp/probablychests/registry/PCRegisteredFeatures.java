package org.cloudwarp.probablychests.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.world.feature.SurfaceChestFeature;

public class PCRegisteredFeatures {

    public static final SurfaceChestFeature SURFACE_CHEST_FEATURE = new SurfaceChestFeature(NoneFeatureConfiguration.CODEC);

    public static void init() {
        Registry.register(BuiltInRegistries.FEATURE, ProbablyChests.id("surface_chest"), SURFACE_CHEST_FEATURE);
    }
}
