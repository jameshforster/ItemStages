package com.tol.researchstages.recipes;

import net.minecraft.item.crafting.IRecipe;

public class StagedRecipe<T extends IRecipe<?>> {
    public final T originalRecipe;
    public final T stagedRecipe;

    public StagedRecipe(T originalRecipe, T stagedRecipe) {
        this.originalRecipe = originalRecipe;
        this.stagedRecipe = stagedRecipe;
    }
}
