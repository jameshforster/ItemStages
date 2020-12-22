package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.tol.itemstages.stages.ItemStageUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionRemoveAndSaveRecipeFactory {
    private List<ResourceLocation> nameList = new ArrayList<>();
    private IRecipeManager manager;

    public ActionRemoveAndSaveRecipeFactory(IRecipeManager manager) {
        this.manager = manager;
    }

//    public ActionRemoveAndSaveRecipe GenerateActionFromNames(List<ResourceLocation> nameList) {
//        this.nameList.addAll(nameList);
//
//        return getBaseAction(playerEntity);
//    }

    public ActionRemoveAndSaveRecipe generateActionFromOutputs(List<ItemStack> itemStacks, PlayerEntity playerEntity) {
        for (Map.Entry<ResourceLocation, IRecipe<?>> recipe : manager.getRecipes().entrySet()) {
            if (itemStacks.contains(ItemStageUtils.INSTANCE.findMatchingItemStack(recipe.getValue().getRecipeOutput()))) {
                nameList.add(recipe.getKey());
            }
        }

        playerEntity.sendStatusMessage(new StringTextComponent("REMOVING THE FOLLOWING RECIPES: " + nameList), false);

        return getBaseAction(playerEntity);
    }

    private ActionRemoveAndSaveRecipe getBaseAction(PlayerEntity playerEntity) {
        return new ActionRemoveAndSaveRecipe(manager, nameList, playerEntity);
    }
}
