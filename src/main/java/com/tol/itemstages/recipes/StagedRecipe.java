package com.tol.itemstages.recipes;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class StagedRecipe implements IRecipe<IInventory> {
    public final List<String> stages;
    public final IRecipe<IInventory> recipe;

    public StagedRecipe(List<String> stages, IRecipe<IInventory> recipe) {
        this.stages = stages;
        this.recipe = recipe;
    }

    public boolean hasStages() {
        if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
            boolean hasAllStages = true;
            PlayerEntity player = Minecraft.getInstance().player;
            for (String stage : stages) {
                if (!GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(stage)) {
                    hasAllStages = false;
                }
            }
            return hasAllStages;
        }

        return true;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        if (hasStages()) {
            return recipe.getCraftingResult(inv);
        } else return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return recipe.canFit(width, height);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        return recipe.getRemainingItems(inv);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }

    @Override
    public boolean isDynamic() {
        return recipe.isDynamic();
    }

    @Override
    public String getGroup() {
        return recipe.getGroup();
    }

    @Override
    public ItemStack getIcon() {
        return recipe.getIcon();
    }

    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return recipe.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return recipe.getType();
    }
}
