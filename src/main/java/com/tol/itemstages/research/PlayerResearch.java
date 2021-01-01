package com.tol.itemstages.research;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PlayerResearch implements IPlayerResearch {

	public PlayerResearch() {}

	public PlayerResearch(CompoundNBT nbt) {
		HashMap<ResearchStage, BigDecimal> playerResearch = new HashMap<>();
		HashMap<ResearchStage, List<ItemStack>> researchedItems = new HashMap<>();
		for (Map.Entry<String, ResearchStage> researchStage: ResearchStageUtils.RESEARCH_STAGES.entrySet()) {
			long progress = nbt.getLong("research_" + researchStage.getKey());
			INBT items = nbt.get("researched_items_" + researchStage.getKey());
			List<ItemStack> itemStacks = new ArrayList<>();
			if (items instanceof ListNBT) {
				for (INBT item : (ListNBT) items) {
					itemStacks.add(ItemStack.read(((CompoundNBT) item)));
				}
			}

			if (progress > 0) {
				researchedItems.put(researchStage.getValue(), itemStacks);
				playerResearch.put(researchStage.getValue(), new BigDecimal(progress));
			}
		}

		this.research = playerResearch;
		this.researchedItems = researchedItems;
	}

	private HashMap<ResearchStage, BigDecimal> research = new HashMap<>();
	private HashMap<ResearchStage, List<ItemStack>> researchedItems = new HashMap<>();

	public BigDecimal getProgress(ResearchStage researchStage) {
		return research.getOrDefault(researchStage, new BigDecimal(0));
	}

	public HashMap<ResearchStage, BigDecimal> getResearch() {return this.research;};

	public void setResearch(HashMap<ResearchStage, BigDecimal> input) {
		this.research.putAll(input);
	}

	public HashMap<ResearchStage, List<ItemStack>> getResearchedItems() {return this.researchedItems;};

	public void setResearchedItems(HashMap<ResearchStage, List<ItemStack>> input) {this.researchedItems.putAll(input);}

	public void updateResearch(ResearchStage researchStage, BigDecimal progress) {
		BigDecimal currentProgress = research.getOrDefault(researchStage, new BigDecimal(0));

		research.put(researchStage, currentProgress.add(progress));
	}

	public void removeResearch(ResearchStage researchStage) {
		research.remove(researchStage);
	}

	public void updateResearchedItem(ResearchStage researchStage, ItemStack itemStack) {
		List<ItemStack> currentResearchedItems = researchedItems.getOrDefault(researchStage, new ArrayList<>());
		currentResearchedItems.add(itemStack);

		researchedItems.put(researchStage, currentResearchedItems);
	}
}
