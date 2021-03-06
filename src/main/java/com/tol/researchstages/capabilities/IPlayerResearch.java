package com.tol.researchstages.capabilities;

import com.tol.researchstages.research.ResearchStage;
import net.minecraft.item.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IPlayerResearch {

	public BigDecimal getProgress(ResearchStage researchStage);

	public HashMap<ResearchStage, BigDecimal> getResearch();

	public void setResearch(HashMap<ResearchStage, BigDecimal> input);

	public HashMap<String, List<ItemStack>> getResearchedItems();

	public void setResearchedItems(HashMap<String, List<ItemStack>> input);

	public void updateResearch(ResearchStage researchStage, BigDecimal progress);

	public void removeResearch(ResearchStage researchStage);

	public void updateResearchedItem(ResearchStage researchStage, ItemStack itemStack);
}
