package org.cloudwarp.probablychests.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements PlayerEntityAccess {
	@Shadow public abstract ServerLevel serverLevel();

	HashSet<UUID> petMimicList = new HashSet<>();

	public ServerPlayerEntityMixin (Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}


	@Override
	public void addPetMimicToOwnedList (UUID mimic) {
		petMimicList.add(mimic);
	}

	public boolean checkForMimicLimit () {
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			PCTameablePetWithInventory entity = (PCTameablePetWithInventory) this.serverLevel().getEntity(mimic);
			if (entity == null || entity.isRemoved()) {
				i.remove();
			}
		}
		if (ProbablyChests.loadedConfig.mimicSettings.doPetMimicLimit && getNumberOfPetMimics() >= ProbablyChests.loadedConfig.mimicSettings.petMimicLimit) {
			return true;
		}
		return false;
	}

	@Override
	public void removePetMimicFromOwnedList (UUID mimic) {
		petMimicList.remove(mimic);
	}

	@Override
	public int getNumberOfPetMimics () {
		return petMimicList.size();
	}

	@Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
	public void writeCustomDataToNbt (CompoundTag nbt, CallbackInfo ci) {
		ListTag listnbt = new ListTag();
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			CompoundTag compoundnbt = new CompoundTag();
			compoundnbt.putUUID("uuid", mimic);
			listnbt.add(compoundnbt);

		}
		nbt.put("pet_mimics", listnbt);
	}

	@Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
	public void readCustomDataFromNbt (CompoundTag nbt, CallbackInfo ci) {
		ListTag listnbt = nbt.getList("pet_mimics", 10);
		for (int i = 0; i < listnbt.size(); ++ i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			addPetMimicToOwnedList(compoundnbt.getUUID("uuid"));
		}
	}

	HashSet<UUID> mimicKeepList = new HashSet<>();

	public void addMimicToKeepList (UUID mimic) {
		mimicKeepList.add(mimic);
	}

	public void removeMimicFromKeepList (UUID mimic) {
		mimicKeepList.remove(mimic);
	}

	public boolean isMimicInKeepList (UUID mimic) {
		return mimicKeepList.contains(mimic);
	}

	@Override
	public int abandonMimics () {
		int removed = 0;
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			if (! isMimicInKeepList(mimic)) {
				PCTameablePetWithInventory entity = (PCTameablePetWithInventory) (this.serverLevel()).getEntity(mimic);
				if (entity != null && ! entity.isRemoved()) {
					entity.setIsAbandoned(true);
				}
				i.remove();
				removed++;
			}
		}
		return removed;
	}
}
