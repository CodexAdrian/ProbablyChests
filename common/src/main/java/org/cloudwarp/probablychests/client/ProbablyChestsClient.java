package org.cloudwarp.probablychests.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.client.entity.PCChestRenderer;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.network.ConfigPacket;
import org.cloudwarp.probablychests.network.MimicInventoryPacket;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screen.PCChestScreen;
import org.cloudwarp.probablychests.screen.PCMimicScreen;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

@Environment(EnvType.CLIENT)
public class ProbablyChestsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		ClientPlayNetworking.registerGlobalReceiver(ConfigPacket.ID, (payload, context) -> {
			context.client().execute(() -> ProbablyChests.loadedConfig = ProbablyChests.nbtToConfig(payload.config()));
		});
		ClientPlayNetworking.registerGlobalReceiver(MimicInventoryPacket.ID, (payload, context) -> {
			// Read packet data on the event loop
			int id = payload.entityId();
			context.client().execute(() -> {
				LocalPlayer player = context.client().player;
				Entity entity = player.level().getEntity(id);
				if (entity instanceof PCTameablePetWithInventory mimicEntity && player.containerMenu instanceof PCMimicScreenHandler mimicScreenHandler) {
					mimicScreenHandler.setMimicEntity(mimicEntity);
				}
			});
		});

		MenuScreens.register(PCScreenHandlerType.PC_CHEST, PCChestScreen::new);
		MenuScreens.register(PCScreenHandlerType.PC_CHEST_MIMIC, PCMimicScreen::new);

		BlockEntityRenderers.register(PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.LUSH.name));
		BlockEntityRenderers.register(PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.NORMAL.name));
		BlockEntityRenderers.register(PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.ROCKY.name));
		BlockEntityRenderers.register(PCBlockEntities.STONE_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.STONE.name));
		BlockEntityRenderers.register(PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.GOLD.name));
		BlockEntityRenderers.register(PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.NETHER.name));
		BlockEntityRenderers.register(PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.SHADOW.name));
		BlockEntityRenderers.register(PCBlockEntities.ICE_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.ICE.name));
		BlockEntityRenderers.register(PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.CORAL.name));
		//---------------------------------------------
		EntityRendererRegistry.register(PCEntities.NORMAL_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "normal_mimic"));
		EntityRendererRegistry.register(PCEntities.LUSH_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "lush_mimic"));
		EntityRendererRegistry.register(PCEntities.ROCKY_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "rocky_mimic"));
		EntityRendererRegistry.register(PCEntities.STONE_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "stone_mimic"));
		EntityRendererRegistry.register(PCEntities.GOLD_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "gold_mimic"));
		EntityRendererRegistry.register(PCEntities.NETHER_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "nether_mimic"));
		EntityRendererRegistry.register(PCEntities.SHADOW_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "shadow_mimic"));
		EntityRendererRegistry.register(PCEntities.ICE_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "ice_mimic"));
		EntityRendererRegistry.register(PCEntities.CORAL_CHEST_MIMIC, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "coral_mimic"));
		//---------------------------------------------
		EntityRendererRegistry.register(PCEntities.NORMAL_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "normal_mimic"));
		EntityRendererRegistry.register(PCEntities.LUSH_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "lush_mimic"));
		EntityRendererRegistry.register(PCEntities.ROCKY_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "rocky_mimic"));
		EntityRendererRegistry.register(PCEntities.STONE_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "stone_mimic"));
		EntityRendererRegistry.register(PCEntities.GOLD_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "gold_mimic"));
		EntityRendererRegistry.register(PCEntities.NETHER_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "nether_mimic"));
		EntityRendererRegistry.register(PCEntities.SHADOW_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "shadow_mimic"));
		EntityRendererRegistry.register(PCEntities.ICE_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "ice_mimic"));
		EntityRendererRegistry.register(PCEntities.CORAL_CHEST_MIMIC_PET, (EntityRendererProvider.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "coral_mimic"));
	}
}
