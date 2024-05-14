package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCItemGroups {

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(ProbablyChests.MOD_ID, "probablychests"));

    public static void init() {
        Registry.register(
                Registries.ITEM_GROUP,
                ITEM_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.probablychests.probablychests"))
                        .icon(() -> new ItemStack(PCBlocks.NORMAL_CHEST))
                        .build()
        );
    }
}
