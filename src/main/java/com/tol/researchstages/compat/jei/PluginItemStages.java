package com.tol.researchstages.compat.jei;

import com.tol.researchstages.ConfigurationHandler;
import com.tol.researchstages.recipes.StagedRecipe;
import com.tol.researchstages.utils.ItemStageUtils;
import com.tol.researchstages.utils.RecipeStageUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin()
public class PluginItemStages implements IModPlugin {
	public static IJeiRuntime runtime;
	public static IIngredientManager ingredientManager;
	public static IRecipeManager recipeManager;

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		runtime = jeiRuntime;
		ingredientManager = jeiRuntime.getIngredientManager();
		recipeManager = jeiRuntime.getRecipeManager();
	}

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation("researchstages");
	}

	@OnlyIn(Dist.CLIENT)
	public static void syncHiddenItems(PlayerEntity player) {
		LogManager.getLogger().info("Configuration for JEI: " + ConfigurationHandler.JEIRestrictionsEnabled.get());
		if (player != null && player.getEntityWorld().isRemote && ConfigurationHandler.JEIRestrictionsEnabled.get()) {
			Collection<ItemStack> hiddenCollection = new ArrayList<>();
			Collection<ItemStack> revealCollection = new ArrayList<>();
			for (ItemStack itemStack : ItemStageUtils.INSTANCE.ITEM_STAGES.keySet()) {
				if (ItemStageUtils.INSTANCE.hasAllStages(player, itemStack)) {
					revealCollection.add(itemStack);
				} else {
					hiddenCollection.add(itemStack);
				}
			}

			if (!hiddenCollection.isEmpty() && ingredientManager != null) {
				ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM, hiddenCollection);
			}

			if (!revealCollection.isEmpty() && ingredientManager != null) {
				ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM, revealCollection);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void syncHiddenRecipes(PlayerEntity player) {
		if (player != null && player.getEntityWorld().isRemote && ConfigurationHandler.JEIRestrictionsEnabled.get()) {
			for (String resourceLocation : RecipeStageUtils.INSTANCE.STAGED_RECIPES_NAMES.keySet()) {
                StagedRecipe<?> recipe = RecipeStageUtils.INSTANCE.STAGED_RECIPES.get(resourceLocation);
                if (RecipeStageUtils.INSTANCE.hasAllStages(player, resourceLocation) && recipeManager != null) {
                    recipeManager.unhideRecipe(recipe.originalRecipe, recipe.originalRecipe.getId());
                } else if (recipeManager != null) {
                    recipeManager.hideRecipe(recipe.originalRecipe, recipe.originalRecipe.getId());
                }
			}
		}
	}
}
