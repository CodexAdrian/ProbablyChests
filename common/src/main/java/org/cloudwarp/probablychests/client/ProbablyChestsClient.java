package org.cloudwarp.probablychests.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.NotImplementedException;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.client.entity.PCChestRenderer;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screen.PCChestScreen;
import org.cloudwarp.probablychests.screen.PCMimicScreen;

import java.util.function.Supplier;

public class ProbablyChestsClient {
	public void onInitializeClient () {
		MenuScreens.register(PCScreenHandlerType.PC_CHEST, PCChestScreen::new);
		MenuScreens.register(PCScreenHandlerType.PC_CHEST_MIMIC, PCMimicScreen::new);

		BlockEntityRenderers.register(PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.LUSH.name));
		BlockEntityRenderers.register(PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.NORMAL.name));
		BlockEntityRenderers.register(PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.ROCKY.name));
		BlockEntityRenderers.register(PCBlockEntities.STONE_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.STONE.name));
		BlockEntityRenderers.register(PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.GOLD.name));
		BlockEntityRenderers.register(PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.NETHER.name));
		BlockEntityRenderers.register(PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.SHADOW.name));
		BlockEntityRenderers.register(PCBlockEntities.ICE_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.ICE.name));
		BlockEntityRenderers.register(PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.CORAL.name));
		//---------------------------------------------
		register(PCEntities.NORMAL_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "normal_mimic"));
		register(PCEntities.LUSH_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "lush_mimic"));
		register(PCEntities.ROCKY_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "rocky_mimic"));
		register(PCEntities.STONE_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "stone_mimic"));
		register(PCEntities.GOLD_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "gold_mimic"));
		register(PCEntities.NETHER_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "nether_mimic"));
		register(PCEntities.SHADOW_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "shadow_mimic"));
		register(PCEntities.ICE_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "ice_mimic"));
		register(PCEntities.CORAL_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "coral_mimic"));
		//-------------------------------
		register(PCEntities.NORMAL_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "normal_mimic"));
		register(PCEntities.LUSH_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "lush_mimic"));
		register(PCEntities.ROCKY_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "rocky_mimic"));
		register(PCEntities.STONE_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "stone_mimic"));
		register(PCEntities.GOLD_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "gold_mimic"));
		register(PCEntities.NETHER_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "nether_mimic"));
		register(PCEntities.SHADOW_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "shadow_mimic"));
		register(PCEntities.ICE_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "ice_mimic"));
		register(PCEntities.CORAL_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "coral_mimic"));
	}

	@ExpectPlatform
	public static <T extends Entity> void register(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> provider) {
		throw new NotImplementedException("ProbablyChestsClient.register");
	}
}
