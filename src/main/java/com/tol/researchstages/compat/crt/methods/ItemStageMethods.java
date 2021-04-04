package com.tol.researchstages.compat.crt.methods;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.tol.researchstages.compat.crt.actions.ActionAddItemRestriction;
import com.tol.researchstages.compat.crt.actions.ActionRemoveItemRestriction;
import com.tol.researchstages.utils.ItemStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages.items")
public class ItemStageMethods {
    @ZenCodeType.Method
    public static void addItemStage(String stage, IIngredient input) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack));
        }
    }

    @ZenCodeType.Method
    public static void addItemStage(String stage, IIngredient input, boolean includeRecipeIngredients) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack, true, includeRecipeIngredients));
        }
    }

    @ZenCodeType.Method
    public static void addManagerItemStage(String stage, IIngredient input, IRecipeManager recipeManager, boolean includeRecipeIngredients) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, itemStack, recipeManager, includeRecipeIngredients));
        }
    }

    @ZenCodeType.Method
    public static void removeItemStage(IIngredient input) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionRemoveItemRestriction(itemStack));
        }
    }

    @ZenCodeType.Method
    public static void stageMod(String stage, String modId) {
        stageMod(stage, modId, true, true);
    }

    @ZenCodeType.Method
    public static void stageMod(String stage, String modId, boolean removeRecipes, boolean includeRecipeIngredients) {
        for (final Item item: ForgeRegistries.ITEMS.getValues()) {
            if (item != null && item != Items.AIR && item.getCreatorModId(item.getDefaultInstance()).equals(modId)) {
                CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, item, removeRecipes, includeRecipeIngredients));
            }
        }
    }

    @ZenCodeType.Method
    public static void setStagedItemName(String name, IIngredient input) {
        for (IItemStack itemStack : input.getItems()) {
            ItemStageUtils.INSTANCE.updateHiddenName(name, itemStack.getInternal());
        }
    }

    @ZenCodeType.Method
    public static void removeItem(IIngredient input) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionAddItemRestriction("removed", itemStack, true, true));
        }
    }

    @ZenCodeType.Method
    public static void removeItemWithModCrafting(IIngredient input, IRecipeManager recipeManager) {
        for (IItemStack itemStack : input.getItems()) {
            CraftTweakerAPI.apply(new ActionAddItemRestriction("removed", itemStack, true, true));
            CraftTweakerAPI.apply(new ActionAddItemRestriction("removed", itemStack, recipeManager, true));
        }
    }
}
