package org.cloudwarp.probablychests.client.entity;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.client.entity.model.PCChestBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PCChestRenderer extends GeoBlockRenderer<PCBaseChestBlockEntity> {

	public PCChestRenderer (String texture) {
		super(new PCChestBlockModel(texture));
	}

	@Override
	public RenderType getRenderType (PCBaseChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityCutoutNoCull(getTextureLocation(animatable), false);
	}

}