package org.cloudwarp.probablychests.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.*;

import java.util.function.Supplier;

import static org.cloudwarp.probablychests.registry.PCBlocks.*;

public class PCBlockEntities {
	public static final ResourcefulRegistry<BlockEntityType<?>> REGISTRY = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ProbablyChests.MOD_ID);

	public static void init () {
		REGISTRY.init();
	}

	//--------------------------------------------------------------
	public static final Supplier<BlockEntityType<LushChestBlockEntity>>   LUSH_CHEST_BLOCK_ENTITY    = REGISTRY.register("lush_chest_block_entity",    () -> BlockEntityType.Builder.of(LushChestBlockEntity::new,     LUSH_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<NormalChestBlockEntity>> NORMAL_CHEST_BLOCK_ENTITY  = REGISTRY.register("normal_chest_block_entity",  () -> BlockEntityType.Builder.of(NormalChestBlockEntity::new,   NORMAL_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<RockyChestBlockEntity>>  ROCKY_CHEST_BLOCK_ENTITY   = REGISTRY.register("rocky_chest_block_entity",   () -> BlockEntityType.Builder.of(RockyChestBlockEntity::new,    ROCKY_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<StoneChestBlockEntity>>  STONE_CHEST_BLOCK_ENTITY   = REGISTRY.register("stone_chest_block_entity",   () -> BlockEntityType.Builder.of(StoneChestBlockEntity::new,    STONE_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<GoldChestBlockEntity>>   GOLD_CHEST_BLOCK_ENTITY    = REGISTRY.register("gold_chest_block_entity",    () -> BlockEntityType.Builder.of(GoldChestBlockEntity::new,     GOLD_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<NetherChestBlockEntity>> NETHER_CHEST_BLOCK_ENTITY  = REGISTRY.register("nether_chest_block_entity",  () -> BlockEntityType.Builder.of(NetherChestBlockEntity::new,   NETHER_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<ShadowChestBlockEntity>> SHADOW_CHEST_BLOCK_ENTITY  = REGISTRY.register("shadow_chest_block_entity",  () -> BlockEntityType.Builder.of(ShadowChestBlockEntity::new,   SHADOW_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<IceChestBlockEntity>>    ICE_CHEST_BLOCK_ENTITY     = REGISTRY.register("ice_chest_block_entity",     () -> BlockEntityType.Builder.of(IceChestBlockEntity::new,      ICE_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<CoralChestBlockEntity>>  CORAL_CHEST_BLOCK_ENTITY   = REGISTRY.register("coral_chest_block_entity",   () -> BlockEntityType.Builder.of(CoralChestBlockEntity::new,    CORAL_CHEST.get()).build(null));
}
