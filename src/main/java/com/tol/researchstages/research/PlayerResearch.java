package com.tol.researchstages.research;

import com.tol.researchstages.capabilities.IPlayerResearch;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerResearch implements IPlayerResearch {

	private HashMap<ResearchStage, BigDecimal> research;
	private HashMap<String, List<ItemStack>> researchedItems;

	public PlayerResearch() {
		this.research = new HashMap<>();
		this.researchedItems = new HashMap<>();
	}

	public PlayerResearch(CompoundNBT nbt) {
		HashMap<ResearchStage, BigDecimal> playerResearch = new HashMap<>();
		HashMap<String, List<ItemStack>> researchedItems = new HashMap<>();
		for (Map.Entry<String, ResearchStage> researchStage : ResearchStageUtils.RESEARCH_STAGES.entrySet()) {
			long progress = nbt.getLong("research_" + researchStage.getKey());
			INBT items = nbt.get("researched_items_" + researchStage.getKey());
			List<ItemStack> itemStacks = new ArrayList<>();
			if (items instanceof ListNBT) {
				for (INBT item : (ListNBT) items) {
					itemStacks.add(ItemStack.read(((CompoundNBT) item)));
				}
			}

			if (progress > 0) {
				researchedItems.put(researchStage.getValue().stageName, itemStacks);
				playerResearch.put(researchStage.getValue(), new BigDecimal(progress));
			}
		}

		this.research = playerResearch;
		this.researchedItems = researchedItems;
	}

	public BigDecimal getProgress(ResearchStage researchStage) {
		return research.getOrDefault(researchStage, new BigDecimal(0));
	}

	public HashMap<ResearchStage, BigDecimal> getResearch() {
		return this.research;
	}

	;

	public void setResearch(HashMap<ResearchStage, BigDecimal> input) {
		this.research = input;
	}

	public HashMap<String, List<ItemStack>> getResearchedItems() {
		return this.researchedItems;
	}

	;

	public void setResearchedItems(HashMap<String , List<ItemStack>> input) {
		this.researchedItems = input;
	}

	public void updateResearch(ResearchStage researchStage, BigDecimal progress) {
		BigDecimal currentProgress = research.getOrDefault(researchStage, new BigDecimal(0));

		research.put(researchStage, currentProgress.add(progress));
	}

	public void removeResearch(ResearchStage researchStage) {
		research.remove(researchStage);
		researchedItems.remove(researchStage.stageName);
	}

	public void updateResearchedItem(ResearchStage researchStage, ItemStack itemStack) {
		List<ItemStack> currentResearchedItems = researchedItems.getOrDefault(researchStage.stageName, new ArrayList<>());
		currentResearchedItems.add(itemStack);

		researchedItems.put(researchStage.stageName, currentResearchedItems);
	}
}
