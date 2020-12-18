package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class ActionRemoveItemRestriction implements IRuntimeAction {

    private final ItemStack itemStack;

    public ActionRemoveItemRestriction(IItemStack itemStack) {
        this.itemStack = itemStack.getInternal();
    }

    @Override
    public void apply() {

    }

    @Override
    public String describe() {
        return null;
    }
}
