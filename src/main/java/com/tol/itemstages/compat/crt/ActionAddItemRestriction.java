package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.tol.itemstages.stages.StageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ActionAddItemRestriction implements IAction {

	private final String stage;
	private final IIngredient ingredient;

	public ActionAddItemRestriction(String stage, IIngredient ingredient) {
		this.stage = stage;
		this.ingredient = ingredient;
	}

	public ActionAddItemRestriction(String stage, Item item) {
		this.stage = stage;
		this.ingredient = IIngredient.fromIngredient(Ingredient.fromItems(item));
	}

	@Override
	public void apply() {
		if (this.stage.isEmpty()) {
			throw new IllegalArgumentException("Empty stage name for this entry");
		}

		ItemStack[] itemStacks;

		itemStacks = CraftTweakerHelper.getItemStacks(ingredient.getItems());

		if (itemStacks.length == 0) {
			throw new IllegalArgumentException("No items or blocks found for this entry");
		}

		for (final ItemStack stack : itemStacks) {
			if (stack.isEmpty()) {
				throw new IllegalArgumentException("Entry contains an empty/air stack");
			}

			StageUtils.ITEM_STAGES.put(stack, this.stage);
		}


	}

	@Override
	public String describe() {
		return "Describe string";
	}
}
