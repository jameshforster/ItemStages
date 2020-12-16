package com.tol.itemstages.compat.jei;

import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.StageUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin()
public class PluginItemStages implements IModPlugin {
	public IIngredientManager ingredientManager;
	public IRecipeManager recipeManager;
	public Collection<ItemStack> ingredients;
	public Collection<FluidStack> fluids;

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		ingredientManager = jeiRuntime.getIngredientManager();
		recipeManager = jeiRuntime.getRecipeManager();
		ingredients = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
		fluids = ingredientManager.getAllIngredients(VanillaTypes.FLUID);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void syncHiddenItems(PlayerEntity player) {

		if (player != null && player.getEntityWorld().isRemote && ConfigurationHandler.hideRestrictionsInJEI) {

			// JEI only allows blacklisting from the main client thread.
//			if (!Minecraft.getInstance().isCallingFromMinecraftThread()) {
//
//				// Reschedules the sync to the correct thread.
//				Minecraft.getInstance().addScheduledTask( () -> syncHiddenItems(player));
//				return;
//			}

			final long time = System.currentTimeMillis();

			final Collection<ItemStack> itemBlacklist = new ArrayList<>();
			final Collection<ItemStack> itemWhitelist = new ArrayList<>();
			final Collection<FluidStack> fluidBlacklist = new ArrayList<>();
			final Collection<FluidStack> fluidWhitelist = new ArrayList<>();

			// Loops through all the known stages
			for (final String key : StageUtils.SORTED_STAGES.keySet()) {

				// Gets all items staged to the current stage.
				final List<ItemStack> entries = StageUtils.SORTED_STAGES.get(key);

				// If player has the stage, it is whitelisted.
				if (GameStageHelper.hasStage(player, key)) {

					itemWhitelist.addAll(entries);
				}

				// If player doesn't have the stage, it is blacklisted.
				else {
					itemBlacklist.addAll(entries);
				}
			}

			for (final String key : StageUtils.FLUID_STAGES.keySet()) {

				if (GameStageHelper.hasStage(player, key)) {

					fluidWhitelist.addAll(StageUtils.FLUID_STAGES.get(key));
				} else {

					fluidBlacklist.addAll(StageUtils.FLUID_STAGES.get(key));
				}
			}

			if (!itemBlacklist.isEmpty()) {

				ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM, itemBlacklist);
			}

			if (!itemWhitelist.isEmpty()) {

				ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM, itemWhitelist);
			}

			if (!fluidBlacklist.isEmpty()) {

				ingredientManager.removeIngredientsAtRuntime(VanillaTypes.FLUID, fluidBlacklist);
			}

			if (!fluidWhitelist.isEmpty()) {

				ingredientManager.addIngredientsAtRuntime(VanillaTypes.FLUID, fluidWhitelist);
			}

			for (final String categoryStage : StageUtils.recipeCategoryStages.keySet()) {

				final boolean hasStage = GameStageHelper.hasStage(player, categoryStage);

				for (final ResourceLocation category : StageUtils.recipeCategoryStages.get(categoryStage)) {

					if (hasStage) {

						recipeManager.unhideRecipeCategory(category);
					} else {

						recipeManager.hideRecipeCategory(category);
					}
				}
			}
		}
	}
}
