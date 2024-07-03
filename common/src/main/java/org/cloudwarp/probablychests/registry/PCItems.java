package org.cloudwarp.probablychests.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.item.*;

import java.util.function.Supplier;

public class PCItems {
	public static final ResourcefulRegistry<Item> REGISTRY = ResourcefulRegistries.create(BuiltInRegistries.ITEM, ProbablyChests.MOD_ID);

	public static final Supplier<Item> MIMIC_KEY = REGISTRY.register("mimic_key", () -> new MimicKeyItem(new Item.Properties().stacksTo(16)));
	public static final Supplier<Item> MIMIC_KEY_FRAGMENT = REGISTRY.register("mimic_key_fragment", () -> new Item(new Item.Properties()));
	public static final Supplier<Item> MIMIC_CORE = REGISTRY.register("mimic_core", () -> new MimicCoreItem(new Item.Properties().stacksTo(1)));
	public static final Supplier<Item> PET_MIMIC_KEY = REGISTRY.register("pet_mimic_key", () -> new PetMimicKeyItem(new Item.Properties().stacksTo(16)));
	public static final Supplier<Item> MIMIC_HAND_BELL = REGISTRY.register("mimic_hand_bell", () -> new MimicHandBellItem(new Item.Properties().stacksTo(1)));
	public static final Supplier<Item> IRON_KEY = REGISTRY.register("iron_key", () -> new IronKeyItem(new Item.Properties().stacksTo(16)));
	public static final Supplier<Item> IRON_LOCK = REGISTRY.register("iron_lock", () -> new IronLockItem(new Item.Properties().stacksTo(16)));

	public static final Supplier<Item> GOLD_KEY = REGISTRY.register("gold_key", () -> new GoldKeyItem(new Item.Properties().stacksTo(16)));
	public static final Supplier<Item> GOLD_LOCK = REGISTRY.register("gold_lock", () -> new GoldLockItem(new Item.Properties().stacksTo(16)));

	public static final Supplier<Item> VOID_KEY = REGISTRY.register("void_key", () -> new VoidKeyItem(new Item.Properties().stacksTo(16)));
	public static final Supplier<Item> VOID_LOCK = REGISTRY.register("void_lock", () -> new VoidLockItem(new Item.Properties().stacksTo(16)));

	public static void init () {
		REGISTRY.init();
	}
}
