package org.cloudwarp.probablychests.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.world.feature.SurfaceChestFeature;

public class PCRegisteredFeatures {

    public static final SurfaceChestFeature SURFACE_CHEST_FEATURE = new SurfaceChestFeature(DefaultFeatureConfig.CODEC);

    public static void init() {
        Registry.register(Registries.FEATURE, ProbablyChests.id("surface_chest"), SURFACE_CHEST_FEATURE);
    }
}
