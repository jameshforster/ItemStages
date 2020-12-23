package com.tol.itemstages.compat.jei;

import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.ItemStageUtils;
import com.tol.itemstages.stages.RecipeStageUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;

import static mezz.jei.api.constants.VanillaRecipeCategoryUid.CRAFTING;

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
		return new ResourceLocation("itemstages");
	}

	@OnlyIn(Dist.CLIENT)
	public static void syncHiddenItems(PlayerEntity player) {

		if (player != null && player.getEntityWorld().isRemote && ConfigurationHandler.hideRestrictionsInJEI) {
			Collection<ItemStack> hiddenCollection = new ArrayList<>();
			Collection<ItemStack> revealCollection = new ArrayList<>();
			for (ItemStack itemStack : ItemStageUtils.INSTANCE.ITEM_STAGES.keySet()) {
				if (ItemStageUtils.INSTANCE.hasAllStages(player, itemStack)) {
					revealCollection.add(itemStack);
				} else {
					hiddenCollection.add(itemStack);
				}
			}

			if (!hiddenCollection.isEmpty()) {
				ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM, hiddenCollection);
			}

			if (!revealCollection.isEmpty()) {
				ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM, revealCollection);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void syncHiddenRecipes(PlayerEntity player) {
		if (player != null && player.getEntityWorld().isRemote && ConfigurationHandler.hideRestrictionsInJEI) {
			for (String resourceLocation : RecipeStageUtils.INSTANCE.STAGED_RECIPES_NAMES.keySet()) {
                IRecipe<?> recipe = RecipeStageUtils.INSTANCE.STAGED_RECIPES.get(resourceLocation);
                if (RecipeStageUtils.INSTANCE.hasAllStages(player, resourceLocation)) {
                    recipeManager.unhideRecipe(recipe, CRAFTING);
                } else {
                    recipeManager.hideRecipe(recipe, CRAFTING);
                }
			}
		}
	}
}
