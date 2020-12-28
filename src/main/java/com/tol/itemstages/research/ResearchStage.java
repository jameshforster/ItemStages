package com.tol.itemstages.research;

import com.tol.itemstages.utils.ItemStackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ResearchStage {
    public String stageName;
    private int experienceCost;
    public List<ItemStack> basicItems = new ArrayList<>();
    public List<ItemStack> advancedItems = new ArrayList<>();

    public ResearchStage(String stageName, int experienceCost, List<ItemStack> basicItems) {
        new ResearchStage(stageName, experienceCost, basicItems, new ArrayList<>());
    }

    public ResearchStage(String stageName, int experienceCost, List<ItemStack> basicItems, List<ItemStack> advancedItems) {
        this.stageName = stageName;
        this.experienceCost = experienceCost;
        this.basicItems.addAll(basicItems);
        this.advancedItems.addAll(advancedItems);
    }

    public boolean containsItem(ItemStack input) {
       return containsBasicItem(input) || containsAdvancedItem(input);
    }

    private boolean containsBasicItem(ItemStack input) {
        return ItemStackUtils.containsItemStack(input, basicItems);
    }

    private boolean containsAdvancedItem(ItemStack input) {
        return ItemStackUtils.containsItemStack(input, advancedItems);
    }

    public int getRequiredExperienceCost(ItemStack input) {
        if (containsBasicItem(input)) {
            return experienceCost;
        } else if (containsAdvancedItem(input) && experienceCost > 5) {
            return experienceCost - 5;
        }

        return 0;
    }
}
