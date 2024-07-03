package org.cloudwarp.probablychests.registry;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public class PCLootTables {

	private static final Set<ResourceLocation> PC_LOOT_TABLES = Sets.newHashSet();

	public static ResourceLocation LUSH_CHEST;
	public static ResourceLocation NORMAL_CHEST;
	public static ResourceLocation ROCKY_CHEST;
	public static ResourceLocation STONE_CHEST;
	public static ResourceLocation GOLD_CHEST;
	public static ResourceLocation NETHER_CHEST;
	public static ResourceLocation SHADOW_CHEST;
	public static ResourceLocation ICE_CHEST;
	public static ResourceLocation CORAL_CHEST;

	private static ResourceLocation register (String id) {
		return PCLootTables.registerLootTable(new ResourceLocation(id));
	}

	private static ResourceLocation registerLootTable (ResourceLocation id) {
		if (PC_LOOT_TABLES.add(id)) {
			return id;
		}
		throw new IllegalArgumentException(id + " is already a registered built-in loot table");
	}

	public static void init () {
		LUSH_CHEST = PCLootTables.register("probablychests:chests/lush_pc_chests");
		NORMAL_CHEST = PCLootTables.register("probablychests:chests/normal_pc_chests");
		ROCKY_CHEST = PCLootTables.register("probablychests:chests/rocky_pc_chests");
		STONE_CHEST = PCLootTables.register("probablychests:chests/stone_pc_chests");
		GOLD_CHEST = PCLootTables.register("probablychests:chests/gold_pc_chests");
		NETHER_CHEST = PCLootTables.register("probablychests:chests/nether_pc_chests");
		SHADOW_CHEST = PCLootTables.register("probablychests:chests/shadow_pc_chests");
		ICE_CHEST = PCLootTables.register("probablychests:chests/ice_pc_chests");
		CORAL_CHEST = PCLootTables.register("probablychests:chests/coral_pc_chests");
	}
}
