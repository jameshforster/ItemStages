package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker.impl.tag.MCTag;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.BlockStageUtils;
import com.tol.itemstages.utils.ItemStageUtils;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.blamejared.crafttweaker.api.CraftTweakerGlobals.println;


@ZenRegister
@ZenCodeType.Name("mods.ResearchStages")
public class CrtMethods {

	@ZenCodeType.Method
	public static void addItemStage(String stage, IIngredient input) {
		for (IItemStack itemStack : input.getItems()) {
			CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack));
		}
	}

	@ZenCodeType.Method
	public static void addItemStage(String stage, IIngredient input, boolean includeRecipeIngredients) {
		for (IItemStack itemStack : input.getItems()) {
			CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack, true, includeRecipeIngredients));
		}
	}

	@ZenCodeType.Method
	public static void addManagerItemStage(String stage, IIngredient input, IRecipeManager recipeManager, boolean includeRecipeIngredients) {
		for (IItemStack itemStack : input.getItems()) {
			CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack, recipeManager, includeRecipeIngredients));
		}
	}

	@ZenCodeType.Method
	public static void removeItemStage(IIngredient input) {
		for (IItemStack itemStack : input.getItems()) {
			CraftTweakerAPI.apply(new ActionRemoveItemRestriction(itemStack));
		}
	}

	@ZenCodeType.Method
	public static void stageMod(String stage, String modId) {
		stageMod(stage, modId, true, true);
	}

	@ZenCodeType.Method
	public static void stageMod(String stage, String modId, boolean removeRecipes, boolean includeRecipeIngredients) {
		for (final Item item: ForgeRegistries.ITEMS.getValues()) {
			if (item != null && item != Items.AIR && item.getCreatorModId(item.getDefaultInstance()).equals(modId)) {
				CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, item, removeRecipes, includeRecipeIngredients));
			}
		}
	}

	@ZenCodeType.Method
	public static void setStagedItemName(String name, IIngredient input) {
		for (IItemStack itemStack : input.getItems()) {
			ItemStageUtils.INSTANCE.updateHiddenName(name, itemStack.getInternal());
		}
	}

	@ZenCodeType.Method
	public static void setBlockStage(String name, MCTag<Block> input) {
		for (Block block : input.getElements()) {
			CraftTweakerAPI.apply(new ActionAddBlockRestriction(block, name));
		}
	}

	@ZenCodeType.Method
	public static void setBlockStage(String name, Block input) {
		CraftTweakerAPI.apply(new ActionAddBlockRestriction(input, name));
	}

	@ZenCodeType.Method
	public static void setStagedBlockName(String name, MCTag<Block> input) {
		for (Block block : input.getElements()) {
			BlockStageUtils.INSTANCE.STAGED_BLOCK_NAMES.put(block.getRegistryName(), name);
		}
	}

	@ZenCodeType.Method
	public static void setStagedBlockName(String name, Block input) {
		BlockStageUtils.INSTANCE.STAGED_BLOCK_NAMES.put(input.getRegistryName(), name);
	}

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
	public static void addBasicItems(String stageName, IIngredient[] basicItems) {
		List<ItemStack> basicItemList = new ArrayList<>();
		for (IIngredient ingredient : basicItems) {
			basicItemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
		}
		ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
		if (researchStage != null) {
			for (ItemStack itemstack : basicItemList) {
				researchStage.addBasicItem(itemstack);
			}
		}
	}

	@ZenCodeType.Method
	public static void addAdvancedItems(String stageName, IIngredient[] advancedItems) {
		List<ItemStack> advancedItemList = new ArrayList<>();
		for (IIngredient ingredient : advancedItems) {
			advancedItemList.addAll(Arrays.asList(CraftTweakerHelper.getItemStacks(ingredient.getItems())));
		}
		ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
		if (researchStage != null) {
			for (ItemStack itemstack : advancedItemList) {
				researchStage.addAdvancedItem(itemstack);
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
			researchStage.setDescription(description);
		}
	}
}
