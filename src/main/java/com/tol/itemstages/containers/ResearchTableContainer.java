package com.tol.itemstages.containers;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.registries.ContainerRegistry;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResearchTableContainer extends Container {
	private final IInventory tableInventory = new Inventory(2) {
		public void markDirty() {
			super.markDirty();
			ResearchTableContainer.this.onCraftMatrixChanged(this);
		}
	};
	private final World worldIn;
	private List<ResearchStage> researchOptions = new ArrayList<>();
	private final StonecutterContainer example = null;
	private final EnchantmentContainer example2 = null;

	public ResearchTableContainer(int windowId, World world, PlayerInventory playerInventory, PlayerEntity player) {
		super(ContainerRegistry.RESEARCH_TABLE.get(), windowId);
		this.worldIn = world;
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
		this.researchOptions = ResearchStageUtils.getOrderedValidStages(player, this.getSlot(0).getStack(), playerResearch);
	}

	public List<ResearchStage> getResearchOptions() {
		return this.researchOptions;
	}

	public Optional<ResearchStage> getOptionalResearchOption(int selection) {
		if (selection < 3 && this.researchOptions.size() > selection) {
			Optional.of(this.researchOptions.get(selection));
		}

		return Optional.empty();
	}

	public boolean canResearch(ClientPlayerEntity player, int selection) {
		List<ResearchStage> validResearch;
		IPlayerResearch playerResearch = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
		validResearch = ResearchStageUtils.getOrderedValidStages(player, this.getSlot(0).getStack(), playerResearch);

		return validResearch.size() > selection && validResearch.get(selection).getRequiredExperienceCost(this.getSlot(0).getStack()) <= player.experienceLevel;
	}

	public void doResearch(ClientPlayerEntity player, int selection) {
		IPlayerResearch playerResearch = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
		ResearchStage validResearch = ResearchStageUtils.getOrderedValidStages(player, this.getSlot(0).getStack(), playerResearch).get(selection);
		ResearchStageUtils.doResearch(player, validResearch, this.getSlot(0).getStack());
		onCraftMatrixChanged(this.tableInventory);
	}

	public void onCraftMatrixChanged(IInventory inventoryIn) {
		LogManager.getLogger().info("[RESEARCHSTAGES] Craft Matrix Changed");
		if (inventoryIn == this.tableInventory) {
			ItemStack itemstack = inventoryIn.getStackInSlot(0);
			LogManager.getLogger().info("[RESEARCHSTAGES] Item in itemstack: " + itemstack);
			LogManager.getLogger().info("[RESEARCHSTAGES] Worldin is remote: " + worldIn.isRemote);
			if (worldIn.isRemote) {
				if (!itemstack.isEmpty()) {
					this.setResearchOptions(Minecraft.getInstance().player);
					LogManager.getLogger().info("[RESEARCHSTAGES] New research options: " +  this.researchOptions);
					this.detectAndSendChanges();
				} else {
					this.researchOptions.clear();
				}
			}
		}

	}
}
