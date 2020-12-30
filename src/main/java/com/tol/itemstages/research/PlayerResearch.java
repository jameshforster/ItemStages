package com.tol.itemstages.research;

import com.tol.itemstages.capabilities.ResearchCapability;
import net.minecraft.item.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerResearch {

	public HashMap<ResearchStage, BigDecimal> research = new HashMap<ResearchStage, BigDecimal>();
	public HashMap<ResearchStage, List<ItemStack>> researchedItems = new HashMap<>();

	public BigDecimal getProgress(ResearchStage researchStage) {
		return research.getOrDefault(researchStage, new BigDecimal(0));
	}

	public void setResearch(HashMap<ResearchStage, BigDecimal> input) {
		this.research.putAll(input);
	}

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
