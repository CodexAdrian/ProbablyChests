package org.cloudwarp.probablychests.client.neoforge;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class ProbablyChestsClientImpl {
    public static <T extends Entity> void register(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> provider) {
        EntityRenderers.register(entityType.get(), provider);
    }
}
