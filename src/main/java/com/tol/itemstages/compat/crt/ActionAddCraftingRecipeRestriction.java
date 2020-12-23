package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.recipes.wrappers.WrapperRecipe;
import com.tol.itemstages.recipes.StagedCraftingRecipe;
import com.tol.itemstages.stages.RecipeStageUtils;
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
            ResourceLocation rl = recipe.getRecipe().getId();
            this.recipeNames.add(rl.getNamespace() + ":" +  rl.getPath());
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
                    ResourceLocation rl = recipe.getId();
					this.recipeNames.add(rl.getNamespace() + ":" +  rl.getPath());
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
                RecipeStageUtils.INSTANCE.STAGED_RECIPES_NAMES.put(name, stages);
                getManager().removeByName(name);
                StagedCraftingRecipe newRecipe;
                if (recipe instanceof StagedCraftingRecipe) {
                    IRecipe<CraftingInventory> baseRecipe = ((StagedCraftingRecipe) recipe).recipe;
                    newRecipe = new StagedCraftingRecipe(stages, baseRecipe);
                } else {
                    newRecipe = new StagedCraftingRecipe(stages, (IRecipe<CraftingInventory>) recipe);
                }

                RecipeStageUtils.INSTANCE.STAGED_RECIPES.put(name, newRecipe);
                getManager().getRecipes().put(recipe.getId(), newRecipe);
            }
        }
    }

    @Override
    public String describe() {
        return "Empty description";
    }
}
