package com.tol.researchstages.compat.crt.methods;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages.research")
public class ResearchStageMethods {
    @ZenCodeType.Method
    public static void setupResearchStage(String stageName, int experienceCost, int researchProgress, IIngredient[] basicItems, IIngredient[] advancedItems) {
        List<ItemStack> basicItemList = new ArrayList<>();
        List<ItemStack> advancedItemList = new ArrayList<>();
        for (IIngredient ingredient : basicItems) {
            basicItemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
        }

        for (IIngredient ingredient : advancedItems) {
            advancedItemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
        }
        ResearchStage stage = new ResearchStage(stageName, experienceCost, researchProgress, basicItemList, advancedItemList);
        ResearchStageUtils.RESEARCH_STAGES.put(stageName, stage);
    }

    @ZenCodeType.Method
    public static void setupResearchStage(String stageName, int experienceCost, int researchProgress) {
        ResearchStage stage = new ResearchStage(stageName, experienceCost, researchProgress);
        ResearchStageUtils.RESEARCH_STAGES.put(stageName, stage);
    }

    @ZenCodeType.Method
    public static void addDefaultItems(String stageName, IIngredient[] basicItems) {
        List<ItemStack> basicItemList = new ArrayList<>();
        for (IIngredient ingredient : basicItems) {
            basicItemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
        }
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            for (ItemStack itemstack : basicItemList) {
                researchStage.addDefaultItem(itemstack);
            }
        }
    }

    @ZenCodeType.Method
    public static void addResearchItems(String stageName, IIngredient[] items, int experienceCost, int researchProgress) {
        List<ItemStack> itemList = new ArrayList<>();
        for (IIngredient ingredient : items) {
            itemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
        }
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            for (ItemStack itemStack : itemList) {
                researchStage.addResearchItem(itemStack, experienceCost, researchProgress);
            }
        }
    }

    @ZenCodeType.Method
    public static void setResearchDescription(String stageName, String description) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.description = description;
        }
    }

    @ZenCodeType.Method
    public static void setResearchLevel(String stageName, int level) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.requiredLevel = level;
        }
    }
}
