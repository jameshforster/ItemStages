package com.tol.itemstages.compat.jei;

import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.ItemStageUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;

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
}
