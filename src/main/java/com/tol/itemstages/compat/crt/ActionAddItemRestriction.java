package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.tol.itemstages.stages.ItemStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ActionAddItemRestriction implements IRuntimeAction {

    private final String stage;
    private final ItemStack itemStack;

    public ActionAddItemRestriction(String stage, IItemStack itemStack) {
        this.stage = stage;
        this.itemStack = itemStack.getInternal();
    }

    public ActionAddItemRestriction(String stage, Item item) {
        this.stage = stage;
        this.itemStack = item.getDefaultInstance();
    }

    @Override
    public void apply() {
        if (this.stage.isEmpty()) {
            throw new IllegalArgumentException("Empty stage name for this entry");
        }

        ItemStageUtils.INSTANCE.updateStages(stage, itemStack);
    }

    @Override
    public String describe() {
        return "Describe string";
    }
}
