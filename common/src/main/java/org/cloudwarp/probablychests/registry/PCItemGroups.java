package org.cloudwarp.probablychests.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.cloudwarp.probablychests.ProbablyChests;

import java.util.function.Supplier;

public class PCItemGroups {
    public static final ResourcefulRegistry<CreativeModeTab> REGISTRY = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, ProbablyChests.MOD_ID);

    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(ProbablyChests.MOD_ID, "probablychests"));

    public static final Supplier<CreativeModeTab> TAB = REGISTRY.register("probablychests", () -> CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM,0).title(Component.translatable("itemGroup.probablychests.probablychests")).icon(() -> new ItemStack(PCBlocks.NORMAL_CHEST.get())).build());

    public static void init() {
        REGISTRY.init();
    }
}
