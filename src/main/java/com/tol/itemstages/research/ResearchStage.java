package com.tol.itemstages.research;

import com.tol.itemstages.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ResearchStage {
    public String stageName;
    public String description = "";
    private int experienceCost;
    private int researchValue;
    public List<ItemStack> basicItems = new ArrayList<>();
    public List<ItemStack> advancedItems = new ArrayList<>();

    public ResearchStage(String stageName, int experienceCost, int researchValue) {
        new ResearchStage(stageName, experienceCost, researchValue, new ArrayList<>(), new ArrayList<>());
    }

    public ResearchStage(String stageName, int experienceCost, int researchValue, List<ItemStack> basicItems, List<ItemStack> advancedItems) {
        this.stageName = stageName;
        this.experienceCost = experienceCost;
        this.researchValue = researchValue;
        this.basicItems.addAll(basicItems);
        this.advancedItems.addAll(advancedItems);
    }

    public void addBasicItem(ItemStack input) {
        if (!containsBasicItem(input)) {
            this.basicItems.add(input);
        }
    }

    public void addAdvancedItem(ItemStack input) {
        if (!containsAdvancedItem(input)) {
            this.advancedItems.add(input);
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean containsItem(ItemStack input) {
       return containsBasicItem(input) || containsAdvancedItem(input);
    }

    public boolean containsBasicItem(ItemStack input) {
        return ItemStackUtils.containsItemStack(input, basicItems);
    }

    public boolean containsAdvancedItem(ItemStack input) {
        return ItemStackUtils.containsItemStack(input, advancedItems);
    }

    public int getRequiredExperienceCost(ItemStack input) {
        if (containsBasicItem(input)) {
            return experienceCost;
        }

        if (containsAdvancedItem(input) && experienceCost > 5) {
            return experienceCost - 5;
        }

        return 0;
    }

    public int returnResearchGained(ItemStack input) {
		if (containsBasicItem(input)) {
			return researchValue;
		}

		if (containsAdvancedItem(input)) {
			return researchValue * 2;
		}

		return 0;
	}

	public String getDescriptiveName() {
        return this.stageName.substring(0, 1).toUpperCase() + this.stageName.substring(1) + " Research";
    }
}
