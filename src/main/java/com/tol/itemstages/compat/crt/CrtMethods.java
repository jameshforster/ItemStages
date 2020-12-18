package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.ItemStages")
public class CrtMethods {

	@ZenCodeType.Method
	public static void addItemStage(String stage, IItemStack input) {
		CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, input));
	}

	@ZenCodeType.Method
	public static void removeItemStage(IItemStack input) {
		CraftTweakerAPI.apply(new ActionRemoveItemRestriction(input));
	}

	@ZenCodeType.Method
	public static void stageModItems(String stage, String modId) {
		for (final Item item: ForgeRegistries.ITEMS.getValues()) {
			if (item != null && item != Items.AIR && item.getCreatorModId(item.getDefaultInstance()).equals(modId)) {
				CraftTweakerAPI.apply(new ActionAddItemRestriction(stage, item));
			}
		}
	}
}
