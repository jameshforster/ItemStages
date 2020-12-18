package com.tol.itemstages.stages;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerRegistry;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker.impl.managers.CTCraftingTableManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeStageUtils {

	public final static RecipeStageUtils INSTANCE = new RecipeStageUtils();

//	public HashMap<IRecipe, String> STAGED_RECIPES = new HashMap<>();

	public void removeRecipesByOutput(ItemStack itemStack) {
		List<ItemStack> originalItemstack = new ArrayList<>();
		originalItemstack.add(itemStack);

		IItemStack convertedItemstack = CraftTweakerHelper.getIItemStacks(originalItemstack).get(0);
		CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(CTCraftingTableManager.INSTANCE, convertedItemstack));
	}
}
