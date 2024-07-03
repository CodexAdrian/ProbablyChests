package org.cloudwarp.probablychests.screenhandlers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;

public class PCChestScreenHandler extends AbstractContainerMenu {
	private static final int columns = 9;
	private final Container inventory;
	private final int rows = 6;
	public PCChestScreenHandler (int syncId, Inventory playerInventory){
		this(PCScreenHandlerType.PC_CHEST,syncId,playerInventory,new SimpleContainer(54));
	}


	public static PCChestScreenHandler createScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		return new PCChestScreenHandler(PCScreenHandlerType.PC_CHEST, syncId, playerInventory, inventory);
	}

	public PCChestScreenHandler (MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
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

	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}

	@Override
	public void removed (Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}

	/*public void close(PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}*/

	public Container getInventory() {
		return this.inventory;
	}

	public int getRows() {
		return this.rows;
	}

}