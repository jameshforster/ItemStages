package com.tol.itemstages.stages;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StageUtils {

    public final static StageUtils INSTANCE = new StageUtils();

    public HashMap<ItemStack, List<String>> ITEM_STAGES = new HashMap<>();
    public HashMap<ItemStack, String> ITEM_HIDDEN_NAMES = new HashMap<>();

    public boolean hasAllStages(PlayerEntity player, ItemStack itemStack) {
		boolean passesValidation = true;

		for (String stage: getStages(findMatchingItemStack(itemStack))) {
			if(!GameStageHelper.hasStage(player, stage)) {
				passesValidation = false;
			}
		}

		return passesValidation;
	}

	public List<String> getStages(ItemStack itemStack) {
		return ITEM_STAGES.getOrDefault(itemStack, new ArrayList<>());
	}

	public ItemStack getStagedItem(ItemStack itemStack) {
    	if(hasStagedItem(itemStack)) {
			return findMatchingItemStack(itemStack);
		}
		return null;
	}

	public boolean hasStagedItem(ItemStack itemStack) {
		ItemStack key = findMatchingItemStack(itemStack);
		return ITEM_STAGES.containsKey(key);
	}

	private ItemStack findMatchingItemStack(ItemStack itemStack) {
		ItemStack key = itemStack;

		for (ItemStack storedItemStack : ITEM_STAGES.keySet()) {
			if (storedItemStack.isItemEqual(itemStack)) {
				key = storedItemStack;
			}
		}

		return key;
	}

	public void updateStages(String stage, ItemStack itemStack) {
    	ItemStack key = findMatchingItemStack(itemStack);
    	List<String> stages = getStages(key);
    	if(!stages.contains(stage)) {
			stages.add(stage);
    		ITEM_STAGES.put(key, stages);
		}
	}

	public String getHiddenName(ItemStack itemStack) {
		ItemStack key = findMatchingItemStack(itemStack);
		return ITEM_HIDDEN_NAMES.getOrDefault(key, "Unknown item");
	}

	public void updateHiddenName(String name, ItemStack itemStack) {
		ItemStack key = findMatchingItemStack(itemStack);
		ITEM_HIDDEN_NAMES.put(key, name);
	}
}
