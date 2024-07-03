package org.cloudwarp.probablychests.interfaces;

import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

import java.util.UUID;
import net.minecraft.world.Container;

public interface PlayerEntityAccess {

	void openMimicInventory (PCTameablePetWithInventory horse, Container inventory);

	void addPetMimicToOwnedList (UUID mimic);

	void removePetMimicFromOwnedList (UUID mimic);

	int abandonMimics ();

	int getNumberOfPetMimics ();

	boolean checkForMimicLimit();

	void addMimicToKeepList(UUID mimic);
	void removeMimicFromKeepList(UUID mimic);
	boolean isMimicInKeepList(UUID mimic);
}
