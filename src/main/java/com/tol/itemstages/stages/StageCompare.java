package com.tol.itemstages.stages;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class StageCompare {

	public static final StageCompare INSTANCE = new StageCompare();

	public boolean isValid(ItemStack entryStack, Object second) {

		if (second instanceof ItemStack) {

			final ItemStack stack = (ItemStack) second;

			final CompoundNBT first = entryStack.getOrCreateTag();
			final CompoundNBT two = stack.getOrCreateTag();

			return ((!this.isTagEmpty(stack) && arePartiallySimilar(first, two)) || (this.isTagEmpty(stack) && this.isTagEmpty(entryStack))) && stack == entryStack;
		}

		return false;
	}

	private boolean isTagEmpty(ItemStack stack) {

		return !stack.hasTag() || stack.getTag().keySet().isEmpty();
	}

	/**
	 * Checks if two NBT types are partially similar. For two tags to be similar, tag one must
	 * have every value that tag two has. Tag two is not required to have any of tag one's
	 * values.
	 *
	 * @param one The first tag to check.
	 * @param two The second tag to check, this is the tag containing required NBT data.
	 * @return Whether or not the tags are partially similar.
	 */
	public static boolean arePartiallySimilar(CompoundNBT one, CompoundNBT two) {

		// First tag can not be null.
		if (one == null) {
			return false;
		}

		// If the comparison is null or empty, default to true.
		if (two == null || two.isEmpty()) {
			return true;
		}

		// If tag is a compound, check each key on the second tag.

		for (final String key : two.keySet()) {

			// Recursively check all the tags on two for partial similarity.
			if (!(one.get(key) == two.get(key))) {
				// Fail if any tag on two is not partially similar to the counterpart on
				// one.
				return false;
			}
		}

		// If all tags on two are partially similar with one, return true.
		return true;
	}
}
