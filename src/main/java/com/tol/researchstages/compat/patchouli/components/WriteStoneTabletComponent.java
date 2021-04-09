package com.tol.researchstages.compat.patchouli.components;

import net.minecraft.item.ItemStack;

import static com.tol.researchstages.registries.ItemRegistry.STONE_TABLET;

public class WriteStoneTabletComponent extends WriteResearchComponent {
    @Override
    ItemStack getDefaultItemStack() {
        return STONE_TABLET.get().getDefaultInstance();
    }
}
