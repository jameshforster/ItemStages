package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.recipes.wrappers.WrapperRecipe;
import com.tol.itemstages.recipes.ProxyRecipe;
import com.tol.itemstages.recipes.StagedRecipe;
import com.tol.itemstages.utils.RecipeStageUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.blamejared.crafttweaker.api.CraftTweakerGlobals.println;

public class ActionAddCraftingRecipeRestriction extends ActionRecipeBase {
    String stage;
    List<String> recipeNames = new ArrayList<>();

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

    @Override
    public void apply() {
        for (String name : recipeNames) {
            List<String> stages = RecipeStageUtils.INSTANCE.STAGED_RECIPES_NAMES.getOrDefault(name, new ArrayList<>());
			IRecipe<?> recipe = getManager().getRecipes().get(new ResourceLocation(name));
            if (recipe != null) {
                if (!stages.contains(stage)) {
                    stages.add(stage);
                }

                if (Arrays.toString(recipe.getClass().getInterfaces()).contains("interface javassist.util.proxy.ProxyObject")) {
                    recipe = RecipeStageUtils.INSTANCE.STAGED_RECIPES.get(name).originalRecipe;
                }

                getManager().removeByName(name);
                IRecipe<?> newRecipe;
                newRecipe = new ProxyRecipe<>(recipe, stages).stagedRecipe;

                RecipeStageUtils.INSTANCE.STAGED_RECIPES_NAMES.put(name, stages);
                RecipeStageUtils.INSTANCE.STAGED_RECIPES.put(name, new StagedRecipe<>(recipe, newRecipe));
                getManager().getRecipes().put(recipe.getId(), newRecipe);
            }
        }
    }

    @Override
    public String describe() {
        return "Empty description";
    }
}
