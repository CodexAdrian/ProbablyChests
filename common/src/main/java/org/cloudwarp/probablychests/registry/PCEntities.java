package org.cloudwarp.probablychests.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

import java.util.function.Supplier;

public class PCEntities {
    public static final ResourcefulRegistry<EntityType<?>> REGISTRY = ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, ProbablyChests.MOD_ID);

    public static final Supplier<EntityType<PCChestMimic>> NORMAL_CHEST_MIMIC = REGISTRY.register(
        "normal_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> LUSH_CHEST_MIMIC = REGISTRY.register(
        "lush_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> ROCKY_CHEST_MIMIC = REGISTRY.register(
        "rocky_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> STONE_CHEST_MIMIC = REGISTRY.register(
        "stone_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> GOLD_CHEST_MIMIC = REGISTRY.register(
        "gold_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> NETHER_CHEST_MIMIC = REGISTRY.register(
        "nether_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> SHADOW_CHEST_MIMIC = REGISTRY.register(
        "shadow_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> ICE_CHEST_MIMIC = REGISTRY.register(
        "ice_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimic>> CORAL_CHEST_MIMIC = REGISTRY.register(
        "coral_chest_mimic",
        () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER).sized(0.9f, 0.9f).build(null)
    );

    //---------------------------------------

    public static final Supplier<EntityType<PCChestMimicPet>> NORMAL_CHEST_MIMIC_PET = REGISTRY.register(
        "normal_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> LUSH_CHEST_MIMIC_PET = REGISTRY.register(
        "lush_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> ROCKY_CHEST_MIMIC_PET = REGISTRY.register(
        "rocky_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> STONE_CHEST_MIMIC_PET = REGISTRY.register(
        "stone_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> GOLD_CHEST_MIMIC_PET = REGISTRY.register(
        "gold_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> NETHER_CHEST_MIMIC_PET = REGISTRY.register(
        "nether_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> SHADOW_CHEST_MIMIC_PET = REGISTRY.register(
        "shadow_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> ICE_CHEST_MIMIC_PET = REGISTRY.register(
        "ice_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final Supplier<EntityType<PCChestMimicPet>> CORAL_CHEST_MIMIC_PET = REGISTRY.register(
        "coral_chest_mimic_pet",
        () -> EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE).sized(0.9f, 0.9f).build(null)
    );

    public static final EntityDataSerializer<PCTameablePetWithInventory.MimicState> STATE_SERIALIZER = EntityDataSerializer.forValueType(PCTameablePetWithInventory.MimicState.NETWORK_CODEC);

    public static void init() {
        EntityDataSerializers.registerSerializer(STATE_SERIALIZER);
    }
}
