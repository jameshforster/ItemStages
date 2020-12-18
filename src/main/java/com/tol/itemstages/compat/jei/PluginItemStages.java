package com.tol.itemstages.compat.jei;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.StageUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.runtime.JeiRuntime;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
			for (ItemStack itemStack : StageUtils.INSTANCE.ITEM_STAGES.keySet()) {
				if (StageUtils.INSTANCE.hasAllStages(player, itemStack)) {
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
