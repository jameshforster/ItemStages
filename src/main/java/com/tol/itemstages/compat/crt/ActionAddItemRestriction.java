package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker.impl.managers.CTCraftingTableManager;
import com.tol.itemstages.utils.ItemStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ActionAddItemRestriction implements IRuntimeAction {

    private final String stage;
    private final ItemStack itemStack;
    private final boolean removeRecipes;
    private final boolean includeRecipeIngredients;

    public ActionAddItemRestriction(String stage, IItemStack itemStack) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
        this.removeRecipes = true;
        this.includeRecipeIngredients = true;
    }

    public ActionAddItemRestriction(String stage, IItemStack itemStack, boolean removeRecipes, boolean includeRecipeIngredients) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
        this.removeRecipes = removeRecipes;
        this.includeRecipeIngredients = includeRecipeIngredients;
    }

    public ActionAddItemRestriction(String stage, Item item, boolean removeRecipes, boolean includeRecipeIngredients) {
        this.stage = stage;
        this.itemStack = item.getDefaultInstance();
        this.removeRecipes = removeRecipes;
        this.includeRecipeIngredients = includeRecipeIngredients;
    }

    @Override
    public void apply() {
        if (this.stage.isEmpty()) {
            throw new IllegalArgumentException("Empty stage name for this entry");
        }
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.add(itemStack);
        IItemStack iItemStack = CraftTweakerHelper.getIItemStacks(itemStackList).get(0);
        ItemStageUtils.INSTANCE.updateStages(stage, itemStack);
        if (removeRecipes) {
            CraftTweakerAPI.apply(new ActionAddCraftingRecipeRestriction(CTCraftingTableManager.INSTANCE, stage, iItemStack, includeRecipeIngredients));
        }
    }

    @Override
    public String describe() {
        return "Describe string";
    }
}
