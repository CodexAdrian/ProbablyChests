package org.cloudwarp.probablychests.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.registry.PCRegisteredFeatures;
import org.cloudwarp.probablychests.utils.PCConfig;

import java.util.List;

public class PCFeatures {

    //--------------------------------------------------------------------------
    public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_CHEST_KEY = registerConfiguredKey("surface_chest");

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        registerConfigured(context, SURFACE_CHEST_KEY, PCRegisteredFeatures.SURFACE_CHEST_FEATURE, new NoneFeatureConfiguration());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ProbablyChests.id(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static final ResourceKey<PlacedFeature> SURFACE_CHEST_PLACED_KEY = registerPlacedKey("surface_chest_placed");

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        PCConfig config = ProbablyChests.loadedConfig;
        float chestSpawnChance = config.worldGen.chestSpawnChance;
        float potSpawnChance = config.worldGen.potSpawnChance;
        float surfaceChestSpawnChance = config.worldGen.surfaceChestSpawnChance;
        var configuredFeatureRegistryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        registerPlaced(context, SURFACE_CHEST_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(SURFACE_CHEST_KEY), PCRarityFilterPlacementModifier.of(surfaceChestSpawnChance * 0.02F), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP);
    }

    public static ResourceKey<PlacedFeature> registerPlacedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ProbablyChests.id(name));
    }

    private static void registerPlaced(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerPlaced(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                                                                                Holder<ConfiguredFeature<?, ?>> configuration,
                                                                                                PlacementModifier... modifiers) {
        registerPlaced(context, key, configuration, List.of(modifiers));
    }
}

