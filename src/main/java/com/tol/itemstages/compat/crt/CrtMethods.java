package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages")
public class CrtMethods {

	@ZenCodeType.Method
	public static void addItemStage(String stage, IItemStack input) {
		CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, input));
	}

	@ZenCodeType.Method
	public static void removeItemStage(IItemStack input) {
		CraftTweakerAPI.apply(new ActionRemoveItemRestriction(input));
	}

	@ZenCodeType.Method
	public static void stageModItems(String stage, String modId) {
		for (final Item item: ForgeRegistries.ITEMS.getValues()) {
			if (item != null && item != Items.AIR && item.getCreatorModId(item.getDefaultInstance()).equals(modId)) {
				CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, item));
			}
		}
	}

	@ZenCodeType.Method
	public static void setupResearchStage(String stageName, int experienceCost, int researchProgress, IItemStack[] basicItems, IItemStack[] advancedItems) {
		List<ItemStack> basicItemList = Arrays.asList(CraftTweakerHelper.getItemStacks(basicItems));
		List<ItemStack> advancedItemList = Arrays.asList(CraftTweakerHelper.getItemStacks(advancedItems));
		ResearchStage stage = new ResearchStage(stageName, experienceCost, researchProgress, basicItemList, advancedItemList);

		ResearchStageUtils.RESEARCH_STAGES.put(stageName, stage);
	}

	@ZenCodeType.Method
	public static void setupResearchStage(String stageName, int experienceCost, int researchProgress) {
		ResearchStage stage = new ResearchStage(stageName, experienceCost, researchProgress);

		ResearchStageUtils.RESEARCH_STAGES.put(stageName, stage);
	}

	@ZenCodeType.Method
	public static void addBasicItems(String stageName, IItemStack[] basicItems) {
		ItemStack[] basicItemList = CraftTweakerHelper.getItemStacks(basicItems);
		ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
		if (researchStage != null) {
			for (ItemStack itemstack : basicItemList) {
				researchStage.addBasicItem(itemstack);
			}
		}
	}

	@ZenCodeType.Method
	public static void addAdvancedItems(String stageName, IItemStack[] advancedItems) {
		ItemStack[] advancedItemList = CraftTweakerHelper.getItemStacks(advancedItems);
		ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
		if (researchStage != null) {
			for (ItemStack itemstack : advancedItemList) {
				researchStage.addAdvancedItem(itemstack);
			}
		}
	}

	@ZenCodeType.Method
	public static void setResearchDescription(String stageName, String description) {
		ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
		if (researchStage != null) {
			researchStage.setDescription(description);
		}
	}
}
