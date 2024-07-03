package org.cloudwarp.probablychests.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestBlock;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.block.PCPot;
import org.cloudwarp.probablychests.block.PCPotTypes;

import java.util.function.Supplier;

public class PCBlocks {
    private static final ResourcefulRegistry<Block> REGISTRY = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, ProbablyChests.MOD_ID);

    //---------------------------

    public static final Supplier<Block> LUSH_CHEST = REGISTRY.register("lush_chest", () -> new PCChestBlock(PCChestTypes.LUSH.setting(), PCChestTypes.LUSH));
    public static final Supplier<Block> NORMAL_CHEST = REGISTRY.register("normal_chest", () -> new PCChestBlock(PCChestTypes.NORMAL.setting(), PCChestTypes.NORMAL));
    public static final Supplier<Block> ROCKY_CHEST = REGISTRY.register("rocky_chest", () -> new PCChestBlock(PCChestTypes.ROCKY.setting(), PCChestTypes.ROCKY));
    public static final Supplier<Block> STONE_CHEST = REGISTRY.register("stone_chest", () -> new PCChestBlock(PCChestTypes.STONE.setting(), PCChestTypes.STONE));
    public static final Supplier<Block> GOLD_CHEST = REGISTRY.register("gold_chest", () -> new PCChestBlock(PCChestTypes.GOLD.setting(), PCChestTypes.GOLD));
    public static final Supplier<Block> NETHER_CHEST = REGISTRY.register("nether_chest", () -> new PCChestBlock(PCChestTypes.NETHER.setting(), PCChestTypes.NETHER));
    public static final Supplier<Block> SHADOW_CHEST = REGISTRY.register("shadow_chest", () -> new PCChestBlock(PCChestTypes.SHADOW.setting(), PCChestTypes.SHADOW));
    public static final Supplier<Block> ICE_CHEST = REGISTRY.register("ice_chest", () -> new PCChestBlock(PCChestTypes.ICE.setting(), PCChestTypes.ICE));
    public static final Supplier<Block> CORAL_CHEST = REGISTRY.register("coral_chest", () -> new PCChestBlock(PCChestTypes.CORAL.setting(), PCChestTypes.CORAL));

    //---------------------------

    public static final Supplier<Block> LUSH_POT = REGISTRY.register("lush_pot", () -> new PCPot(PCPotTypes.LUSH.setting(), PCVoxelShapes.POT_VOXELSHAPE));
    public static final Supplier<Block> NORMAL_POT = REGISTRY.register("normal_pot", () -> new PCPot(PCPotTypes.NORMAL.setting(), PCVoxelShapes.POT_VOXELSHAPE));
    public static final Supplier<Block> ROCKY_POT = REGISTRY.register("rocky_pot", () -> new PCPot(PCPotTypes.ROCKY.setting(), PCVoxelShapes.POT_VOXELSHAPE));
    public static final Supplier<Block> NETHER_POT = REGISTRY.register("nether_pot", () -> new PCPot(PCPotTypes.NETHER.setting(), PCVoxelShapes.POT_VOXELSHAPE));

    // ------------------------------

    public static void init() {
        REGISTRY.init();
        REGISTRY.stream().forEach(item -> PCItems.REGISTRY.register(
                item.getId().getPath(),
                () -> new BlockItem(item.get(), new Item.Properties())
        ));
    }
}
