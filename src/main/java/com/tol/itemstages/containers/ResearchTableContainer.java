package com.tol.itemstages.containers;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;

public class ResearchTableContainer extends Container {
	private final IInventory tableInventory = new Inventory(1);
	private final IWorldPosCallable worldPosCallable;

	public ResearchTableContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, IWorldPosCallable.DUMMY);
	}

	public ResearchTableContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(ContainerRegistry.RESEARCH_TABLE, id);
		this.worldPosCallable = worldPosCallable;
		this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
			public int getSlotStackLimit() {
				return 1;
			}
		});

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.ENCHANTING_TABLE);
	}
}
