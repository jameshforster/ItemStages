package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import com.blamejared.crafttweaker.impl.recipes.wrappers.WrapperRecipe;
import com.tol.itemstages.stages.ItemStageUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.world.NoteBlockEvent;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionRemoveAndSaveRecipe implements IAction {

    private List<ResourceLocation> names;
    private PlayerEntity player;
    public static List<IRecipe> recipeList = new ArrayList<>();
    private final IRecipeManager manager;

    public ActionRemoveAndSaveRecipe(IRecipeManager manager, List<ResourceLocation> names, PlayerEntity playerEntity) {
        this.names = names;
        this.player = playerEntity;
        this.manager = manager;
    }

    @Override
    public void apply() {
        for (ResourceLocation recipeName : names) {
            player.sendStatusMessage(new StringTextComponent("ACTION REMOVING RECIPE:" + recipeName), false);
            recipeList.add(manager.getRecipes().get(recipeName));
            player.sendStatusMessage(new StringTextComponent("ACTION RECIPE SAVED TO LIST FOR:" + recipeName), false);
            manager.getRecipes().remove(recipeName);
        }
    }

    @Override
    public String describe() {
        return "Empty description";
    }
}
