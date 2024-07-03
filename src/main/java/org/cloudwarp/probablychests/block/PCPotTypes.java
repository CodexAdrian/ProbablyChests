package org.cloudwarp.probablychests.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.cloudwarp.probablychests.ProbablyChests;

public enum PCPotTypes {
    LUSH(new ResourceLocation(ProbablyChests.MOD_ID, "lush_pot")),
    NORMAL(new ResourceLocation(ProbablyChests.MOD_ID, "normal_pot")),
    ROCKY(new ResourceLocation(ProbablyChests.MOD_ID, "rocky_pot")),
    NETHER(new ResourceLocation(ProbablyChests.MOD_ID, "nether_pot"));

    public final ResourceLocation texture;

    PCPotTypes(ResourceLocation texture) {

        this.texture = texture;

    }

    public BlockBehaviour.Properties setting() {
        return switch (this) {
            case LUSH, NORMAL -> BlockBehaviour.Properties.of()
                    .destroyTime(1.0F)
                    .explosionResistance(1.0F)
                    .sound(SoundType.BONE_BLOCK)
                    .instabreak()
                    .noOcclusion();
            case ROCKY, NETHER -> BlockBehaviour.Properties.of()
                    .destroyTime(1.0F)
                    .explosionResistance(1.0F)
                    .sound(SoundType.BASALT)
                    .instabreak()
                    .noOcclusion();
            default -> BlockBehaviour.Properties.of();
        };
    }
}
