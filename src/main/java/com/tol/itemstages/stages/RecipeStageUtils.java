package com.tol.itemstages.stages;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.impl.managers.CTCraftingTableManager;
import com.tol.itemstages.compat.crt.ActionRemoveAndSaveRecipeFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.*;

public class RecipeStageUtils {

	public final static RecipeStageUtils INSTANCE = new RecipeStageUtils();

//	public HashMap<IRecipe, String> STAGED_RECIPES = new HashMap<>();

	private void removeStagedRecipesByOutput(List<ItemStack> itemstacks, PlayerEntity playerEntity) {
		CraftTweakerAPI.apply(new ActionRemoveAndSaveRecipeFactory(CTCraftingTableManager.INSTANCE).generateActionFromOutputs(itemstacks, playerEntity));
	}

	public void syncHiddenRecipes(PlayerEntity playerEntity) {
		List<ItemStack> removedItems = new ArrayList<>();
		for(ItemStack stagedItem :ItemStageUtils.INSTANCE.ITEM_STAGES.keySet()) {
			if (!ItemStageUtils.INSTANCE.hasAllStages(playerEntity, stagedItem)) {
				playerEntity.sendStatusMessage(new StringTextComponent("REMOVING RECIPE FOR " + stagedItem), false);
				removedItems.add(stagedItem);
			}
		}

		removeStagedRecipesByOutput(removedItems, playerEntity);
	}
}
