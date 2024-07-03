package org.cloudwarp.probablychests.item;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class MimicCoreItem extends Item {
	public MimicCoreItem (Properties settings) {
		super(settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.probablychests.mimic_core.tooltip"));
	}
}
