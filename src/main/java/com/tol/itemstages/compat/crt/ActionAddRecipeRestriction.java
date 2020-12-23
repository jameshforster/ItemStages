package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.recipes.wrappers.WrapperRecipe;
import com.tol.itemstages.recipes.StagedCraftingRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;

public class ActionAddRecipeRestriction extends ActionRecipeBase {
    String stage;
    List<String> recipeNames = new ArrayList<>();

    public ActionAddRecipeRestriction(IRecipeManager manager, String stage, IItemStack itemStack) {
        super(manager);
        this.stage = stage;
        for (WrapperRecipe recipe : getManager().getRecipesByOutput(itemStack)) {
            this.recipeNames.add(recipe.getRecipe().getId().getPath());
        }
    }

    public ActionAddRecipeRestriction(IRecipeManager manager, String stage, List<String> recipeNames) {
        super(manager);
        this.stage = stage;
        this.recipeNames.addAll(recipeNames);
    }

    @Override
    public void apply() {
        for (String name : recipeNames) {
            List<String> stages = new ArrayList<>();
            WrapperRecipe recipe = getManager().getRecipeByName(name);
            if (ICraftingRecipe.class.isAssignableFrom(recipe.getRecipe().getClass())) {
                if (recipe.getRecipe() instanceof StagedCraftingRecipe) {
                    stages.addAll(((StagedCraftingRecipe) recipe.getRecipe()).stages);
                }
                if (!stages.contains(stage)) {
                    stages.add(stage);
                }
                getManager().removeByName(name);
                if (recipe.getRecipe() instanceof StagedCraftingRecipe) {
                    IRecipe<CraftingInventory> baseRecipe = ((StagedCraftingRecipe) recipe.getRecipe()).recipe;
                    getManager().getRecipes().put(recipe.getRecipe().getId(), new StagedCraftingRecipe(stages, baseRecipe));
                } else {
                    StagedCraftingRecipe temp = new StagedCraftingRecipe(stages, (IRecipe<CraftingInventory>) recipe.getRecipe());
                    getManager().getRecipes().put(recipe.getRecipe().getId(), temp);
                }
            }
        }
    }

    @Override
    public String describe() {
        return "Empty description";
    }
}
