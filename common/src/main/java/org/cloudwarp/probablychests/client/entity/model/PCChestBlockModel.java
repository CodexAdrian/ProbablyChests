package org.cloudwarp.probablychests.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class PCChestBlockModel extends GeoModel<PCBaseChestBlockEntity> {

	private static final ResourceLocation MODEL_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "geo/pc_chest_block.json");
	private static final ResourceLocation ANIMATION_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "animations/pc_chest_block.animation.json");
	private final ResourceLocation TEXTURE_IDENTIFIER;

	public PCChestBlockModel (String texture) {
		TEXTURE_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "textures/block/" + texture + ".png");
	}

	@Override
	public ResourceLocation getTextureResource (PCBaseChestBlockEntity entity) {
		return TEXTURE_IDENTIFIER;
	}

	@Override
	public ResourceLocation getModelResource (PCBaseChestBlockEntity entity) {
		return MODEL_IDENTIFIER;
	}

	@Override
	public ResourceLocation getAnimationResource (PCBaseChestBlockEntity entity) {
		return ANIMATION_IDENTIFIER;
	}
}