package com.tol.researchstages.compat.crt.actions;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker.impl.managers.CTCraftingTableManager;
import com.tol.researchstages.utils.ItemStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ActionAddItemRestriction implements IRuntimeAction {

    private final String stage;
    private final ItemStack itemStack;
    private final IRecipeManager recipeManager;
    private final boolean includeRecipeIngredients;

    public ActionAddItemRestriction(String stage, IItemStack itemStack) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
        this.recipeManager = null;
        this.includeRecipeIngredients = false;
    }

    public ActionAddItemRestriction(String stage, IItemStack itemStack, IRecipeManager recipeManager, boolean includeRecipeIngredients) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
        this.recipeManager = recipeManager;
        this.includeRecipeIngredients = includeRecipeIngredients;
    }

    public ActionAddItemRestriction(String stage, IItemStack itemStack, boolean removeRecipes, boolean includeRecipeIngredients) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
        if (removeRecipes) {
            this.recipeManager = CTCraftingTableManager.INSTANCE;
        } else {
            this.recipeManager = null;
        }
        this.includeRecipeIngredients = includeRecipeIngredients;
    }

    public ActionAddItemRestriction(String stage, Item item, boolean removeRecipes, boolean includeRecipeIngredients) {
        this.stage = stage;
        this.itemStack = item.getDefaultInstance();
        if (removeRecipes) {
            this.recipeManager = CTCraftingTableManager.INSTANCE;
        } else {
            this.recipeManager = null;
        }
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
        if (recipeManager != null) {
            CraftTweakerAPI.apply(new ActionAddCraftingRecipeRestriction(recipeManager, stage, iItemStack, includeRecipeIngredients));
        }
    }

    @Override
    public String describe() {
        return "Adding item restriction for " + itemStack.getDisplayName() + " for stage: " + stage;
    }
}
