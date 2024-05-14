package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.item.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class PCItems {
	private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
	public static final Item MIMIC_KEY = create(new MimicKeyItem(new Item.Settings().maxCount(16)),"mimic_key");
	public static final Item MIMIC_KEY_FRAGMENT = create(new Item(new Item.Settings()),"mimic_key_fragment");
	public static final Item MIMIC_CORE = create(new MimicCoreItem(new Item.Settings().maxCount(1)),"mimic_core");
	public static final Item PET_MIMIC_KEY = create(new PetMimicKeyItem(new Item.Settings().maxCount(16)),"pet_mimic_key");
	public static final Item MIMIC_HAND_BELL = create(new MimicHandBellItem(new Item.Settings().maxCount(1)),"mimic_hand_bell");
	public static final Item IRON_KEY = create(new IronKeyItem(new Item.Settings().maxCount(16)),"iron_key");
	public static final Item IRON_LOCK = create(new IronLockItem(new Item.Settings().maxCount(16)),"iron_lock");

	public static final Item GOLD_KEY = create(new GoldKeyItem(new Item.Settings().maxCount(16)),"gold_key");
	public static final Item GOLD_LOCK = create(new GoldLockItem(new Item.Settings().maxCount(16)),"gold_lock");

	public static final Item VOID_KEY = create(new VoidKeyItem(new Item.Settings().maxCount(16)),"void_key");
	public static final Item VOID_LOCK = create(new VoidLockItem(new Item.Settings().maxCount(16)),"void_lock");

	public static void init () {
		ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
	}
	public static <T extends Item> T create(T item, String name){
		ITEMS.put(item,ProbablyChests.id(name));
		ItemGroupEvents.modifyEntriesEvent(PCItemGroups.ITEM_GROUP).register(content ->{
			content.add(item);
		});
		return item;
	}
}
