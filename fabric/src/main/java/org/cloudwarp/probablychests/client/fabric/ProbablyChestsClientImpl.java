package org.cloudwarp.probablychests.client.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class ProbablyChestsClientImpl {
    public static <T extends Entity> void register(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> provider) {
        EntityRendererRegistry.register(entityType.get(), provider);
    }
}
