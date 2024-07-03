package org.cloudwarp.probablychests.utils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCStatistics;

import static org.cloudwarp.probablychests.block.PCChestBlock.FACING;
import static org.cloudwarp.probablychests.block.PCChestBlock.WATERLOGGED;

public class PCMimicCreationUtils {

	public static boolean createHostileMimic (Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
		PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			createMimicEntity(false, pos, state, world, chest, player, type);
			return true;
		}
		return false;
	}

	public static void tryMakeHostileMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type){
		if(canCreateHostileMimic(world, pos, state, player, type)){
			createHostileMimic(world, pos, state, player, type);
		}
	}

	public static boolean canCreateHostileMimic (Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
		PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		return (world.getDifficulty() != Difficulty.PEACEFUL && isSecretMimic(chest, world, pos, type));
	}

	public static void createMimicEntity (boolean isPetMimic, BlockPos pos, BlockState state, Level world, PCBaseChestBlockEntity chest, Player player, PCChestTypes type) {
		if (chest.hasMadeMimic) {
			return;
		}
		chest.hasMadeMimic = true;
		PCTameablePetWithInventory mimic;
		if (isPetMimic) {
			mimic = new PCChestMimicPet(type.getPetMimicType(), world);
			mimic.tame(player);
			mimic.setTarget((LivingEntity) null);
			mimic.setOrderedToSit(true);
			((PlayerEntityAccess) player).addPetMimicToOwnedList(mimic.getUUID());
			if (player != null) {
				CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer) player, mimic);
			}
		} else {
			mimic = new PCChestMimic(type.getMimicType(), world);
			if (player != null) {
				player.awardStat(PCStatistics.MIMIC_ENCOUNTERS, 1);
				CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer) player, mimic);
			}
		}
		mimic.setType(type);
		mimic.setPosRaw(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
		mimic.setYRot(state.getValue(FACING).toYRot());
		mimic.yHeadRot = mimic.getYRot();
		mimic.yBodyRot = mimic.getYRot();
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setItem(i, chest.getItem(i));
			chest.setItem(i, ItemStack.EMPTY);
		}
		world.addFreshEntity(mimic);
		if (isPetMimic) {
			mimic.setOrderedToSit(true);
			mimic.level().broadcastEntityEvent(mimic, (byte) 7);
		}
		boolean waterlogged = state.getValue(WATERLOGGED);
		world.setBlockAndUpdate(pos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
	}

	public static void convertPetMimicToHostile (Level world, PCChestTypes type, PCTameablePetWithInventory other) {
		PCTameablePetWithInventory mimic;
		mimic = new PCChestMimic(type.getMimicType(), world);
		mimic.setType(type);
		mimic.copyPosition(other);
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setItem(i, other.inventory.getItem(i));
			other.inventory.setItem(i, ItemStack.EMPTY);
		}
		if (other.hasCustomName()) {
			mimic.setCustomName(other.getCustomName());
			mimic.setCustomNameVisible(other.isCustomNameVisible());
		}
		other.discard();
		world.addFreshEntity(mimic);
	}

	public static void convertHostileMimicToPet (Level world, PCChestTypes type, PCTameablePetWithInventory other, Player player) {
		PCTameablePetWithInventory mimic;
		mimic = new PCChestMimicPet(type.getPetMimicType(), world);
		mimic.tame(player);
		mimic.setTarget((LivingEntity) null);
		mimic.setOrderedToSit(true);
		((PlayerEntityAccess) player).addPetMimicToOwnedList(mimic.getUUID());
		mimic.setType(type);
		mimic.copyPosition(other);
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setItem(i, other.inventory.getItem(i));
			other.inventory.setItem(i, ItemStack.EMPTY);
		}
		if (other.hasCustomName()) {
			mimic.setCustomName(other.getCustomName());
			mimic.setCustomNameVisible(other.isCustomNameVisible());
		}
		other.discard();
		world.addFreshEntity(mimic);
		mimic.level().broadcastEntityEvent(mimic, (byte) 7);
	}

	public static boolean isSecretMimic (PCBaseChestBlockEntity chest, Level world, BlockPos pos, PCChestTypes type) {
		if(world.getDifficulty() == Difficulty.PEACEFUL){
			if (! chest.hasBeenInteractedWith && chest.isNatural) {
				chest.hasBeenInteractedWith = true;
				chest.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, type.getLootTable()));
			}
			return false;
		}
		PCConfig config = ProbablyChests.loadedConfig;
		if (! chest.hasBeenInteractedWith && chest.isNatural) {
			chest.hasBeenInteractedWith = true;
			float mimicRandom = world.getRandom().nextFloat();
			chest.isMimic = mimicRandom < config.worldGen.secretMimicChance;
			if(!chest.isMimic){
				chest.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, type.getLootTable()));
			}
		}
		return chest.isMimic;
	}
	public static boolean canCreatePetMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type){
		PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		return !(((PlayerEntityAccess) player).checkForMimicLimit()) && !isSecretMimic(chest,world,pos,type);
	}

	public static boolean tryMakePetMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type){
		if(canCreatePetMimic(world, pos, state, player, type)){
			return createPetMimic(world, pos, state, player, type);
		}
		return false;
	}

	public static boolean createPetMimic (Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
		PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			createMimicEntity(true, pos, state, world, chest, player, type);
			return true;
		}
		return false;
	}

	public static PCBaseChestBlockEntity getChestBlockFromWorld (Level world, BlockPos pos) {
		PCBaseChestBlockEntity chest = null;
		if (world.getBlockEntity(pos) instanceof PCBaseChestBlockEntity) {
			chest = (PCBaseChestBlockEntity) world.getBlockEntity(pos);
		}
		return chest;
	}
}
