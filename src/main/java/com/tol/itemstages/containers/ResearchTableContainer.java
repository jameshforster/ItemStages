package com.tol.itemstages.containers;

import com.tol.itemstages.registries.ContainerRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ResearchTableContainer extends Container {
	private final IInventory tableInventory = new Inventory(1);
	private final World worldIn;

	public ResearchTableContainer(int windowId, World world, PlayerInventory playerInventory, PlayerEntity player) {
		super(ContainerRegistry.RESEARCH_TABLE.get(), windowId);
		this.worldIn = world;
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
		return true;
	}
}
