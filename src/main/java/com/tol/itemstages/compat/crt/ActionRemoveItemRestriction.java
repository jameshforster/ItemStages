package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.tol.itemstages.stages.StageUtils;
import net.minecraft.item.ItemStack;

public class ActionRemoveItemRestriction implements IAction {

	private final IIngredient ingredient;

	public ActionRemoveItemRestriction(IIngredient ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public void apply() {
		ItemStack[] itemStacks;

		itemStacks = CraftTweakerHelper.getItemStacks(ingredient.getItems());

		if (itemStacks.length == 0) {
			throw new IllegalArgumentException("No items or blocks found for this entry");
		}

		for (final ItemStack stack : itemStacks) {
			if (stack.isEmpty()) {
				throw new IllegalArgumentException("Entry contains an empty/air stack");
			}

			StageUtils.ITEM_STAGES.remove(stack);
		}
	}

	@Override
	public String describe() {
		return null;
	}
}
