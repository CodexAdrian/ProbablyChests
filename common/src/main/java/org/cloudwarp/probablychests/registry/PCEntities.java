package org.cloudwarp.probablychests.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

public class PCEntities {
    public static final EntityType<PCChestMimic> NORMAL_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "normal_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> LUSH_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "lush_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> ROCKY_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "rocky_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    )
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> STONE_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "stone_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> GOLD_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "gold_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> NETHER_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "nether_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> SHADOW_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "shadow_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> ICE_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "ice_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimic> CORAL_CHEST_MIMIC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "coral_chest_mimic"),
            EntityType.Builder.of(
                            PCChestMimic::new,
                            MobCategory.MONSTER
                    ).sized(0.9f, 0.9f)
                    .build(null)
    );
    //---------------------------------------
    public static final EntityType<PCChestMimicPet> NORMAL_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "normal_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> LUSH_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "lush_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> ROCKY_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "rocky_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> STONE_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "stone_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> GOLD_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "gold_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> NETHER_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "nether_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> SHADOW_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "shadow_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> ICE_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "ice_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );
    public static final EntityType<PCChestMimicPet> CORAL_CHEST_MIMIC_PET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ProbablyChests.MOD_ID, "coral_chest_mimic_pet"),
            EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .build(null)
    );

    public static final EntityDataSerializer<PCTameablePetWithInventory.MimicState> STATE_SERIALIZER = EntityDataSerializer.forValueType(PCTameablePetWithInventory.MimicState.NETWORK_CODEC);

    public static void init() {
        EntityDataSerializers.registerSerializer(STATE_SERIALIZER);
    }
}
