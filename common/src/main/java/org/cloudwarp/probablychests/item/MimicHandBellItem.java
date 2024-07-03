package org.cloudwarp.probablychests.item;

import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.registry.PCStatistics;

import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class MimicHandBellItem extends Item {
	public MimicHandBellItem (Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn (UseOnContext context) {

		Level world = context.getLevel();
		BlockPlaceContext itemPlacementContext = new BlockPlaceContext(context);
		BlockPos blockPos = itemPlacementContext.getClickedPos();

		if (world instanceof ServerLevel serverWorld && context.getPlayer() instanceof  ServerPlayer player) {
			blockPos = blockPos.relative(context.getClickedFace().getOpposite());
			if(serverWorld.getBlockState(blockPos).is(Blocks.AMETHYST_CLUSTER)){
				int amount = ((PlayerEntityAccess) player).abandonMimics();
				if(amount > 0){
					player.awardStat(PCStatistics.ABANDONED_MIMICS,amount);
					CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player,blockPos,context.getItemInHand());
				}
				playSound(world,blockPos,PCSounds.BELL_HIT_1);
			}
		}
		return InteractionResult.sidedSuccess(world.isClientSide);
	}
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if(Screen.hasShiftDown()){
			tooltip.add(Component.translatable("item.probablychests.mimicHandBell.tooltip.shift"));
			tooltip.add(Component.translatable("item.probablychests.mimicHandBell.tooltip.shift2"));
			tooltip.add(Component.translatable("item.probablychests.mimicHandBell.tooltip.shift3"));
		}else{
			tooltip.add(Component.translatable("item.probablychests.shift.tooltip"));
		}
	}
	static void playSound (Level world, BlockPos pos, SoundEvent soundEvent) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.5;
		double f = (double) pos.getZ() + 0.5;

		world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.8f, world.random.nextFloat() * 0.1f + 0.9f);
	}
}
