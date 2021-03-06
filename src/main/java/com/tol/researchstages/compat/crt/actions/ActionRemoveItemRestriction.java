package com.tol.researchstages.compat.crt.actions;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.tol.researchstages.utils.ItemStageUtils;
import net.minecraft.item.ItemStack;

public class ActionRemoveItemRestriction implements IRuntimeAction {

    private final ItemStack itemStack;
    private String stage;

    public ActionRemoveItemRestriction(IItemStack itemStack) {
        this.itemStack = itemStack.getInternal();
        this.stage = null;
    }

    public ActionRemoveItemRestriction(IItemStack itemStack, String stage) {
        this.itemStack = itemStack.getInternal();
        this.stage = stage;
    }

    @Override
    public void apply() {
        if (stage != null) {
            if (this.stage.isEmpty()) {
                throw new IllegalArgumentException("Empty stage name for this entry");
            }
            ItemStageUtils.INSTANCE.ITEM_STAGES.get(itemStack).remove(stage);
        } else {
            ItemStageUtils.INSTANCE.ITEM_STAGES.remove(itemStack);
        }
    }

    @Override
    public String describe() {
        return "Removing item restriction for: " + itemStack.getDisplayName() + " for stage: " + stage;
    }
}
