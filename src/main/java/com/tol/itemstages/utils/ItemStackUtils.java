package com.tol.itemstages.utils;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemStackUtils {

    public static boolean containsItemStack(ItemStack input, List<ItemStack> collection) {
        boolean matchedItem = false;

        for (ItemStack itemStack : collection) {
            if (itemStack.isItemEqual(input)) {
                matchedItem = true;
            }
        }

        return matchedItem;
    }
}
