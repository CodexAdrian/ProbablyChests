package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

public class PCEntities {
    public static final EntityType<PCChestMimic> NORMAL_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "normal_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> LUSH_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "lush_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> ROCKY_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "rocky_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn)
                    )
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> STONE_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "stone_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> GOLD_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "gold_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> NETHER_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "nether_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> SHADOW_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "shadow_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> ICE_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "ice_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimic> CORAL_CHEST_MIMIC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "coral_chest_mimic"),
            FabricEntityType.Builder.createMob(
                            PCChestMimic::new,
                            SpawnGroup.MONSTER,
                            mob -> mob.spawnRestriction(
                                    SpawnLocationTypes.ON_GROUND,
                                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                    PCChestMimic::canSpawn
                            )
                    ).dimensions(0.9f, 0.9f)
                    .build()
    );
    //---------------------------------------
    public static final EntityType<PCChestMimicPet> NORMAL_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "normal_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> LUSH_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "lush_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> ROCKY_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "rocky_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> STONE_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "stone_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> GOLD_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "gold_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> NETHER_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "nether_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> SHADOW_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "shadow_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> ICE_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "ice_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );
    public static final EntityType<PCChestMimicPet> CORAL_CHEST_MIMIC_PET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ProbablyChests.MOD_ID, "coral_chest_mimic_pet"),
            EntityType.Builder.create(PCChestMimicPet::new, SpawnGroup.CREATURE)
                    .dimensions(0.9f, 0.9f)
                    .build()
    );

    public static final TrackedDataHandler<PCTameablePetWithInventory.MimicState> STATE_SERIALIZER = TrackedDataHandler.create(PCTameablePetWithInventory.MimicState.NETWORK_CODEC);

    public static void init() {
        FabricDefaultAttributeRegistry.register(NORMAL_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LUSH_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ROCKY_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STONE_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GOLD_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(NETHER_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(SHADOW_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ICE_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CORAL_CHEST_MIMIC, PCChestMimic.createMobAttributes());
        //------------------------------
        FabricDefaultAttributeRegistry.register(NORMAL_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LUSH_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ROCKY_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STONE_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GOLD_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(NETHER_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(SHADOW_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ICE_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CORAL_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());

        TrackedDataHandlerRegistry.register(STATE_SERIALIZER);
    }
}
