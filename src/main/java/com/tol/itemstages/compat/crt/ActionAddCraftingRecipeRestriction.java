package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.recipes.wrappers.WrapperRecipe;
import com.tol.itemstages.recipes.StagedCraftingRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionAddCraftingRecipeRestriction extends ActionRecipeBase {
    String stage;
    List<String> recipeNames = new ArrayList<>();

	private static final Logger LOGGER = LogManager.getLogger();

	public ActionAddCraftingRecipeRestriction(IRecipeManager manager, String stage, IItemStack itemStack, boolean includeIngredients) {
        super(manager);
        this.stage = stage;
        for (WrapperRecipe recipe : getManager().getRecipesByOutput(itemStack)) {
            this.recipeNames.add(recipe.getRecipe().getId().getPath());
        }
        if (includeIngredients) {
			for (IRecipe<?> recipe : getManager().getRecipes().values()) {
				List<ItemStack> ingredientStacks = new ArrayList<>();
				for (Ingredient ingredient : recipe.getIngredients()) {
					ingredientStacks.addAll(Arrays.asList(ingredient.getMatchingStacks()));
				}
				boolean containsStagedItemIngredient = false;
				for (ItemStack ingredientStack : ingredientStacks) {
					if (ingredientStack.isItemEqual(itemStack.getInternal())) {
						containsStagedItemIngredient = true;
					}
				}

				if (containsStagedItemIngredient) {
					this.recipeNames.add(recipe.getId().getPath());
				}
			}
		}
    }

    public ActionAddCraftingRecipeRestriction(IRecipeManager manager, String stage, List<String> recipeNames) {
        super(manager);
        this.stage = stage;
        this.recipeNames.addAll(recipeNames);
    }

    @Override
    public void apply() {
		LOGGER.info("[STAGEDMOD] Staging the following recipes: " + recipeNames);
        for (String name : recipeNames) {
            List<String> stages = new ArrayList<>();
			IRecipe<?> recipe = getManager().getRecipes().get(new ResourceLocation(name));
            if (recipe != null && ICraftingRecipe.class.isAssignableFrom(recipe.getClass())) {
                if (recipe instanceof StagedCraftingRecipe) {
                    stages.addAll(((StagedCraftingRecipe) recipe).stages);
                }
                if (!stages.contains(stage)) {
                    stages.add(stage);
                }
                getManager().removeByName(name);
                if (recipe instanceof StagedCraftingRecipe) {
                    IRecipe<CraftingInventory> baseRecipe = ((StagedCraftingRecipe) recipe).recipe;
                    getManager().getRecipes().put(recipe.getId(), new StagedCraftingRecipe(stages, baseRecipe));
                } else {
                    StagedCraftingRecipe temp = new StagedCraftingRecipe(stages, (IRecipe<CraftingInventory>) recipe);
                    getManager().getRecipes().put(recipe.getId(), temp);
                }
            }
            if (recipe == null) {
            	LOGGER.info("[STAGEDMOD] Recipe not found, could not stage: " + name);
			}
        }
    }

    @Override
    public String describe() {
        return "Empty description";
    }
}
