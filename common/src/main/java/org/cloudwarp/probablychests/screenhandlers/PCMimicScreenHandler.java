package org.cloudwarp.probablychests.screenhandlers;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.utils.PCEventHandler;

import java.util.UUID;

public class PCMimicScreenHandler extends AbstractContainerMenu {
	private static final int columns = 9;
	private final Container inventory;
	private final int rows = 6;
	private PCTameablePetWithInventory entity;
	public PCMimicScreenHandler (int syncId, Inventory playerInventory){
		this(PCScreenHandlerType.PC_CHEST_MIMIC,syncId,playerInventory,new SimpleContainer(54));
	}
	public PCMimicScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		this(PCScreenHandlerType.PC_CHEST_MIMIC, syncId, playerInventory, inventory);
	}

	public static PCMimicScreenHandler createScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		return new PCMimicScreenHandler(PCScreenHandlerType.PC_CHEST_MIMIC, syncId, playerInventory, inventory);
	}

	public PCMimicScreenHandler (MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
		super(type, syncId);
		int k;
		int j;
		checkContainerSize(inventory, rows * columns);
		this.inventory = inventory;
		inventory.startOpen(playerInventory.player);
		int i = (this.rows - 4) * 18;

		for (j = 0; j < this.rows; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}
		for (j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
		}

	}

	public void setMimicEntity(PCTameablePetWithInventory entity){
		this.entity = entity;
	}

	@Override
	public ItemStack quickMoveStack (Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < this.rows * 9) {
				if (!this.moveItemStackTo(itemStack2, this.rows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, this.rows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	@Override
	public boolean stillValid(Player player) {
		if(this.entity.getIsMimicLocked() && player != this.entity.getOwner()){
			this.entity.bite(player);
			return false;
		}
		return !this.entity.areInventoriesDifferent(this.inventory) && this.inventory.stillValid(player) && this.entity.isAlive() && this.entity.distanceTo(player) < 8.0f;
	}


	@Override
	public void removed(Player player) {
		if(player instanceof ServerPlayer) {
			if (this.entity != null) {
				entity.closeGui(player);
			} else {
				System.out.println("entity is null");
			}
		}
		super.removed(player);
		this.inventory.stopOpen(player);
	}

	public Container getInventory() {
		return this.inventory;
	}

	public int getRows() {
		return this.rows;
	}

}