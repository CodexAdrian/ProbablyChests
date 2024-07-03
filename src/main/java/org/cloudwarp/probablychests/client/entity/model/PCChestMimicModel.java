package org.cloudwarp.probablychests.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import software.bernie.geckolib.model.GeoModel;

public class PCChestMimicModel extends GeoModel<PCChestMimic> {

	private static final ResourceLocation MODEL_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "geo/pc_chest_mimic.json");
	private static final ResourceLocation ANIMATION_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "animations/pc_chest_mimic.animation.json");
	private final ResourceLocation TEXTURE_IDENTIFIER;

	public PCChestMimicModel (String texture) {
		TEXTURE_IDENTIFIER = new ResourceLocation(ProbablyChests.MOD_ID, "textures/entity/" + texture + ".png");
	}

	@Override
	public ResourceLocation getTextureResource (PCChestMimic entity) {
		return TEXTURE_IDENTIFIER;
	}

	@Override
	public ResourceLocation getModelResource (PCChestMimic entity) {
		return MODEL_IDENTIFIER;
	}

	@Override
	public ResourceLocation getAnimationResource (PCChestMimic entity) {
		return ANIMATION_IDENTIFIER;
	}
}
