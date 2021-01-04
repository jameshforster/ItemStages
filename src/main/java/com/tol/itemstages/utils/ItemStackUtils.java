package com.tol.itemstages.utils;

import net.minecraft.item.ItemStack;

import java.util.Collection;

public class ItemStackUtils {

    public static boolean containsItemStack(ItemStack input, Collection<ItemStack> collection) {
        boolean matchedItem = false;

        for (ItemStack itemStack : collection) {
            if (itemStack.isItemEqual(input)) {
                matchedItem = true;
            }
        }

        return matchedItem;
    }
}
