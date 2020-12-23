package com.tol.itemstages.recipes;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.util.List;

public class StagedCraftingRecipe implements ICraftingRecipe {
    public final List<String> stages;
    public final IRecipe<CraftingInventory> recipe;

    public StagedCraftingRecipe(List<String> stages, IRecipe<CraftingInventory> recipe) {
        this.stages = stages;
        this.recipe = recipe;
    }

    public boolean hasStages() {
        if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.isRemote) {
            boolean hasAllStages = true;
            PlayerEntity player = Minecraft.getInstance().player;
            player.sendStatusMessage(new StringTextComponent("CHECKING RECIPE STAGE"), false);
            player.sendStatusMessage(new StringTextComponent("ALL STAGES: " + stages), false);
            player.sendStatusMessage(new StringTextComponent("FOR PLAYER: " + Minecraft.getInstance().player), false);
            player.sendStatusMessage(new StringTextComponent("EFFECTIVE SIDE: " + EffectiveSide.get().isClient()), false);
            for (String stage : stages) {
                player.sendStatusMessage(new StringTextComponent("CHECKING FOR STAGE: " + stage), false);
                if (!GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(stage)) {
                    hasAllStages = false;
                }
            }
            player.sendStatusMessage(new StringTextComponent("FINAL RESULT: " + hasAllStages), false);
            return hasAllStages;
        } else return true;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
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
