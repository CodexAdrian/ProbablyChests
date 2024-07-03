package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.cloudwarp.probablychests.registry.PCBlocks.*;

public class PCBlockEntities {
	private static final Map<BlockEntityType<?>, ResourceLocation> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static void init () {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}

	private static <T extends BlockEntity> BlockEntityType<T> register (String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new ResourceLocation(ProbablyChests.MOD_ID, name));
		return type;
	}

	//--------------------------------------------------------------
	public static final BlockEntityType<LushChestBlockEntity>   LUSH_CHEST_BLOCK_ENTITY    = register("lush_chest_block_entity",    BlockEntityType.Builder.of(LushChestBlockEntity::new,     LUSH_CHEST).build(null));
	public static final BlockEntityType<NormalChestBlockEntity> NORMAL_CHEST_BLOCK_ENTITY  = register("normal_chest_block_entity",  BlockEntityType.Builder.of(NormalChestBlockEntity::new,   NORMAL_CHEST).build(null));
	public static final BlockEntityType<RockyChestBlockEntity>  ROCKY_CHEST_BLOCK_ENTITY   = register("rocky_chest_block_entity",   BlockEntityType.Builder.of(RockyChestBlockEntity::new,    ROCKY_CHEST).build(null));
	public static final BlockEntityType<StoneChestBlockEntity>  STONE_CHEST_BLOCK_ENTITY   = register("stone_chest_block_entity",   BlockEntityType.Builder.of(StoneChestBlockEntity::new,    STONE_CHEST).build(null));
	public static final BlockEntityType<GoldChestBlockEntity>   GOLD_CHEST_BLOCK_ENTITY    = register("gold_chest_block_entity",    BlockEntityType.Builder.of(GoldChestBlockEntity::new,     GOLD_CHEST).build(null));
	public static final BlockEntityType<NetherChestBlockEntity> NETHER_CHEST_BLOCK_ENTITY  = register("nether_chest_block_entity",  BlockEntityType.Builder.of(NetherChestBlockEntity::new,   NETHER_CHEST).build(null));
	public static final BlockEntityType<ShadowChestBlockEntity> SHADOW_CHEST_BLOCK_ENTITY  = register("shadow_chest_block_entity",  BlockEntityType.Builder.of(ShadowChestBlockEntity::new,   SHADOW_CHEST).build(null));
	public static final BlockEntityType<IceChestBlockEntity>    ICE_CHEST_BLOCK_ENTITY     = register("ice_chest_block_entity",     BlockEntityType.Builder.of(IceChestBlockEntity::new,      ICE_CHEST).build(null));
	public static final BlockEntityType<CoralChestBlockEntity>  CORAL_CHEST_BLOCK_ENTITY   = register("coral_chest_block_entity",   BlockEntityType.Builder.of(CoralChestBlockEntity::new,    CORAL_CHEST).build(null));
}
