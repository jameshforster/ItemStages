package com.tol.itemstages.containers;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.registries.ContainerRegistry;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ResearchTableContainer extends Container {
	private final IInventory tableInventory = new Inventory(2) {
		public void markDirty() {
			super.markDirty();
			ResearchTableContainer.this.onCraftMatrixChanged(this);
		}
	};
	private final World worldIn;
	private List<ResearchStage> researchOptions = new ArrayList<>();
	private int level;

	public ResearchTableContainer(int windowId, World world, PlayerInventory playerInventory, PlayerEntity player, int level) {
		super(ContainerRegistry.RESEARCH_TABLE.get(), windowId);
		this.worldIn = world;
		this.level = level;
		this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
			public int getSlotStackLimit() {
				return 1;
			}
		});
		this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
			public int getSlotStackLimit() {
				return 1;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index == 1) {
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else if (itemstack1.getItem() == Items.LAPIS_LAZULI) {
				if (!this.mergeItemStack(itemstack1, 1, 2, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack1)) {
					return ItemStack.EMPTY;
				}

				ItemStack itemstack2 = itemstack1.copy();
				itemstack2.setCount(1);
				itemstack1.shrink(1);
				this.inventorySlots.get(0).putStack(itemstack2);
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	public void setResearchOptions(ClientPlayerEntity player) {
		IPlayerResearch playerResearch = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
		this.researchOptions = ResearchStageUtils.getOrderedValidStagesForLevel(player, this.getSlot(0).getStack(), playerResearch, level);
	}

	public List<ResearchStage> getResearchOptions() {
		return this.researchOptions;
	}

	public boolean canResearch(ClientPlayerEntity player, int selection) {
		List<ResearchStage> validResearch;
		IPlayerResearch playerResearch = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
		validResearch = ResearchStageUtils.getOrderedValidStagesForLevel(player, this.getSlot(0).getStack(), playerResearch, level);

		return validResearch.size() > selection && validResearch.get(selection).getRequiredExperienceCost(this.getSlot(0).getStack()) <= player.experienceLevel;
	}

	public void doResearch(ClientPlayerEntity player, int selection) {
		IPlayerResearch playerResearch = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
		ResearchStage validResearch = ResearchStageUtils.getOrderedValidStagesForLevel(player, this.getSlot(0).getStack(), playerResearch, level).get(selection);
		ResearchStageUtils.doResearch(player, validResearch, this.getSlot(0).getStack());
		onCraftMatrixChanged(this.tableInventory);
	}

	public void onCraftMatrixChanged(IInventory inventoryIn) {
		if (inventoryIn == this.tableInventory) {
			ItemStack itemstack = inventoryIn.getStackInSlot(0);
			if (worldIn.isRemote) {
				if (!itemstack.isEmpty()) {
					this.setResearchOptions(Minecraft.getInstance().player);
					this.detectAndSendChanges();
				} else {
					this.researchOptions.clear();
				}
			}
		}

	}
}
