package com.tol.itemstages.research;

import com.tol.itemstages.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchStage {
    public String stageName;
    public String description = "";
    public HashMap<ItemStack, ResearchValues> researchItems = new HashMap<>();
    private final ResearchValues basicResearchValuesDefault;
    private final ResearchValues advancedResearchValuesDefault;

    public ResearchStage(String stageName, int defaultExperienceCost, int defaultResearchValue) {
		this.stageName = stageName;

		int calculatedExpCost = 0;
		if (defaultExperienceCost > 5) {
			calculatedExpCost = defaultExperienceCost - 5;
		}

		basicResearchValuesDefault = new ResearchValues(defaultExperienceCost, defaultResearchValue);
		advancedResearchValuesDefault = new ResearchValues(calculatedExpCost, defaultResearchValue * 2);
    }

    public ResearchStage(String stageName, int defaultExperienceCost, int defaultResearchValue, List<ItemStack> basicItems, List<ItemStack> advancedItems) {
        this.stageName = stageName;

		int advancedExpCost = 0;
		if (defaultExperienceCost > 5) {
			advancedExpCost = defaultExperienceCost - 5;
		}

        basicResearchValuesDefault = new ResearchValues(defaultExperienceCost, defaultResearchValue);
        advancedResearchValuesDefault = new ResearchValues(advancedExpCost, defaultResearchValue * 2);

        for (ItemStack basicItem : basicItems) {
        	researchItems.put(basicItem, basicResearchValuesDefault);
		}
        for (ItemStack advancedItem : advancedItems) {
        	researchItems.put(advancedItem, advancedResearchValuesDefault);
		}
    }

    public void addBasicItem(ItemStack input) {
		this.researchItems.put(input, basicResearchValuesDefault);
    }

    public void addAdvancedItem(ItemStack input) {
		this.researchItems.put(input, advancedResearchValuesDefault);
    }

    public void addResearchItem(ItemStack input, int experienceCost, int progressValue) {
    	this.researchItems.put(input, new ResearchValues(experienceCost, progressValue));
	}

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean containsItem(ItemStack input) {
		return ItemStackUtils.containsItemStack(input, this.researchItems.keySet());
    }

    public int getRequiredExperienceCost(ItemStack input) {
    	if (containsItem(input)) {
    		for (Map.Entry<ItemStack, ResearchValues> entry : this.researchItems.entrySet()) {
    			if (entry.getKey().isItemEqual(input)) {
    				return entry.getValue().experienceCost;
				}
			}
		}

        return 0;
    }

    public int returnResearchGained(ItemStack input) {
		if (containsItem(input)) {
			for (Map.Entry<ItemStack, ResearchValues> entry : this.researchItems.entrySet()) {
				if (entry.getKey().isItemEqual(input)) {
					return entry.getValue().progressGiven;
				}
			}
		}

		return 0;
	}

	public String getDescriptiveName() {
        return this.stageName.substring(0, 1).toUpperCase() + this.stageName.substring(1) + " Research";
    }
}
