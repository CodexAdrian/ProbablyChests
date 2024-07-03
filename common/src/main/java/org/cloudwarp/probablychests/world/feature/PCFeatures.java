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

    //--------------------------------------------------
    //private static final Feature<DefaultFeatureConfig> SURFACE_CHEST_FEATURE = new SurfaceChestFeature(DefaultFeatureConfig.CODEC);


	/*public static final RegistryEntry<ConfiguredFeature<?, ?>> SURFACE_CHEST =
			ConfiguredFeatures.register("surface_chest_feature", SURFACE_CHEST_FEATURE, new DefaultFeatureConfig());

	private static final Feature<DefaultFeatureConfig> UNDERGROUND_CHEST_FEATURE = new UndergroundChestFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> UNDERGROUND_CHEST =
			ConfiguredFeatures.register("underground_chest_feature", UNDERGROUND_CHEST_FEATURE, new DefaultFeatureConfig());
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> NETHER_CHEST =
			ConfiguredFeatures.register("nether_chest_feature", UNDERGROUND_CHEST_FEATURE, new DefaultFeatureConfig());
	//------------------------
	private static final Feature<PCPotFeatureConfig> NORMAL_POT_FEATURE = new NormalPotFeature(PCPotFeatureConfig.CODEC);
	//---------------------------------------------------------------------------
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> NORMAL_POT =
			ConfiguredFeatures.register("normal_pot", NORMAL_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> LUSH_POT_FEATURE = new LushPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> LUSH_POT =
			ConfiguredFeatures.register("lush_pot", LUSH_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> ROCKY_POT_FEATURE = new RockyPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> ROCKY_POT =
			ConfiguredFeatures.register("rocky_pot", ROCKY_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> NETHER_POT_FEATURE = new NetherPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> Nether_POT =
			ConfiguredFeatures.register("nether_pot", NETHER_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//---------------------------------------------------------------------------
	public static RegistryEntry<PlacedFeature> SURFACE_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> UNDERGROUND_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_CHEST_PLACED;
	//------------------------
	public static RegistryEntry<PlacedFeature> NORMAL_POT_PLACED;
	public static RegistryEntry<PlacedFeature> LUSH_POT_PLACED;
	public static RegistryEntry<PlacedFeature> ROCKY_POT_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_POT_PLACED;*/

	/*public static void init () {
		PCConfig config = ProbablyChests.loadedConfig;
		float chestSpawnChance = config.worldGen.chestSpawnChance;
		float potSpawnChance = config.worldGen.potSpawnChance;
		float surfaceChestSpawnChance = config.worldGen.surfaceChestSpawnChance;
		//-----------------------------------
		/*SURFACE_CHEST_PLACED = register(ProbablyChests.id("surface_chest_placed"),
				SURFACE_CHEST,
				PCRarityFilterPlacementModifier.of(surfaceChestSpawnChance * 0.02F),
				SquarePlacementModifier.of(),
				PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP
		);
		UNDERGROUND_CHEST_PLACED = register(ProbablyChests.id("underground_chest_placed"),
				UNDERGROUND_CHEST,
				CountPlacementModifier.of(2),
				SquarePlacementModifier.of(),
				PCRarityFilterPlacementModifier.of(chestSpawnChance * 0.85F),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(64)),
				EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 32)
		);
		NETHER_CHEST_PLACED = register(ProbablyChests.id("nether_chest_placed"),
				NETHER_CHEST,
				CountPlacementModifier.of(2),
				SquarePlacementModifier.of(),
				PCRarityFilterPlacementModifier.of(chestSpawnChance * 0.75F),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.belowTop(6)),
				EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 12)
		);
		//------------------------------------------------
		NORMAL_POT_PLACED = register(ProbablyChests.id("normal_pot_placed"),
				NORMAL_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		LUSH_POT_PLACED = register(ProbablyChests.id("lush_pot_placed"),
				LUSH_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		ROCKY_POT_PLACED = register(ProbablyChests.id("rocky_pot_placed"),
				ROCKY_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		NETHER_POT_PLACED = register(ProbablyChests.id("nether_pot_placed"),
				Nether_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());*/
    //-------------------
    //Registry.register(Registries.FEATURE, ProbablyChests.id("pc_surface_chest"), SURFACE_CHEST_FEATURE);
		/*Registry.register(Registries.FEATURE, ProbablyChests.id("pc_underground_chest"), UNDERGROUND_CHEST_FEATURE);
		Registry.register(Registries.FEATURE, ProbablyChests.id("pc_normal_pot"), NORMAL_POT_FEATURE);
		Registry.register(Registries.FEATURE, ProbablyChests.id("pc_lush_pot"), LUSH_POT_FEATURE);
		Registry.register(Registries.FEATURE, ProbablyChests.id("pc_rocky_pot"), ROCKY_POT_FEATURE);
		Registry.register(Registries.FEATURE, ProbablyChests.id("pc_nether_pot"), NETHER_POT_FEATURE);*/
    //}

	/*public static RegistryEntry<PlacedFeature> register (Identifier id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, List<PlacementModifier> modifiers) {
		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(RegistryEntry.upcast(registryEntry), List.copyOf(modifiers)));
	}

	public static RegistryEntry<PlacedFeature> register (Identifier id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, PlacementModifier... modifiers) {
		return register(id, registryEntry, List.of(modifiers));
	}*/
}

