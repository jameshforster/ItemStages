package com.tol.itemstages.stages;

import com.google.common.collect.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class StageUtils {

	public static final HashMap<ItemStack, String> ITEM_STAGES = new HashMap<>();
	public static final ListMultimap<String, FluidStack> FLUID_STAGES = ArrayListMultimap.create();
	public static final ListMultimap<String, ItemStack> SORTED_STAGES = ArrayListMultimap.create();

	public static final SetMultimap<Item, Tuple<ItemStack, String>> SORTED_ITEM_STAGES = Multimaps.newSetMultimap(Maps.newIdentityHashMap(), Sets::newIdentityHashSet);
	public static final Map<EnchantmentData, String> ENCHANT_STAGES = new HashMap<>();
	public static final HashMap<ItemStack, String> CUSTOM_NAMES = new HashMap<>();
	public static final ListMultimap<String, String> tooltipStages = ArrayListMultimap.create();
	public static final ListMultimap<String, ResourceLocation> recipeCategoryStages = ArrayListMultimap.create();

	public static String getStage (ItemStack stack) {

		if (!stack.isEmpty()) {

			for (final Tuple<ItemStack, String> entry : SORTED_ITEM_STAGES.get(stack.getItem())) {

				if (StageCompare.INSTANCE.isValid(stack, entry.getA())) {

					return entry.getB();
				}
			}
		}

		return null;
	}

	public static String getEnchantStage (ItemStack stack) {

		if (!stack.isEmpty()) {

			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

			for (final Map.Entry<Enchantment, Integer> enchant : map.entrySet()) {

				for (final Map.Entry<EnchantmentData, String> enchantStage : ENCHANT_STAGES.entrySet()) {

					if (enchantStage.getKey().enchantment == enchant.getKey() && enchantStage.getKey().enchantmentLevel == enchant.getValue()) {

						return enchantStage.getValue();
					}
				}
			}
		}

		return null;
	}

	public static String getUnfamiliarName (ItemStack stack) {

		return CUSTOM_NAMES.getOrDefault(stack, "Unfamiliar Item");
	}

	public static void sendDropMessage (PlayerEntity player, ItemStack stack) {

		player.sendStatusMessage(new StringTextComponent(getUnfamiliarName(stack)), false);
	}

	public static void sendAttackFailMessage (PlayerEntity player, ItemStack stack) {

		player.sendStatusMessage(stack.getDisplayName(), false);
	}
}
