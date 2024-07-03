package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCItemGroups {

    public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(ProbablyChests.MOD_ID, "probablychests"));

    public static void init() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                ITEM_GROUP,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup.probablychests.probablychests"))
                        .icon(() -> new ItemStack(PCBlocks.NORMAL_CHEST))
                        .build()
        );
    }
}
