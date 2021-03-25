package com.tol.researchstages.research;

import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.compat.patchouli.utils.EntryParameters;
import com.tol.researchstages.utils.ItemStackUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ResearchStage {
    public String stageName;
    public String description = "";
    public HashMap<ItemStack, ResearchValues> researchItems = new HashMap<>();
    private final ResearchValues basicResearchValuesDefault;
    private final ResearchValues advancedResearchValuesDefault;
    public int requiredLevel = 0;
    public final EntryParameters bookParams;

    public ResearchStage(String stageName, int defaultExperienceCost, int defaultResearchValue) {
		this.stageName = stageName;

		int calculatedExpCost = 0;
		if (defaultExperienceCost > 5) {
			calculatedExpCost = defaultExperienceCost - 5;
		}

		basicResearchValuesDefault = new ResearchValues(defaultExperienceCost, defaultResearchValue);
		advancedResearchValuesDefault = new ResearchValues(calculatedExpCost, defaultResearchValue * 2);
		bookParams = new EntryParameters("research_compendium", "research_category");
    }

    public ResearchStage(String stageName, int defaultExperienceCost, int defaultResearchValue, List<ItemStack> basicItems, List<ItemStack> advancedItems) {
        this.stageName = stageName;

		int advancedExpCost = 0;
		if (defaultExperienceCost > 5) {
			advancedExpCost = defaultExperienceCost - 5;
		}

        basicResearchValuesDefault = new ResearchValues(defaultExperienceCost, defaultResearchValue);
        advancedResearchValuesDefault = new ResearchValues(advancedExpCost, defaultResearchValue * 2);

        for (ItemStack basicItem : basicItems) {
        	researchItems.put(basicItem, basicResearchValuesDefault);
		}
        for (ItemStack advancedItem : advancedItems) {
        	researchItems.put(advancedItem, advancedResearchValuesDefault);
		}
		bookParams = new EntryParameters("research_compendium", "research_category");
    }

    public void addDefaultItem(ItemStack input) {
		this.researchItems.put(input, basicResearchValuesDefault);
    }

    public void addResearchItem(ItemStack input, int experienceCost, int progressValue) {
    	this.researchItems.put(input, new ResearchValues(experienceCost, progressValue));
	}

    public boolean containsItem(ItemStack input) {
		return ItemStackUtils.containsItemStack(input, this.researchItems.keySet());
    }

    public List<ItemStack> getUnresearchedItems(ClientPlayerEntity playerEntity) {
    	if (GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName) || !bookParams.displayCondition.checkCondition(playerEntity)) {
    		return new ArrayList<>();
		}
    	ArrayList<ItemStack> remainingItems = new ArrayList<>();
    	for (ItemStack item: researchItems.keySet()) {
    		if (!ItemStackUtils.containsItemStack(item, playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH).map(cap ->
					cap.getResearchedItems().getOrDefault(stageName, new ArrayList<>())
					).orElseGet(ArrayList::new))) {
    			remainingItems.add(item);
			}
		}
    	return remainingItems;
	}

	public List<ItemStack> getResearchedItems(ClientPlayerEntity playerEntity) {
		if (GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName)) {
			return new ArrayList<>(researchItems.keySet());
		}
		AtomicReference<List<ItemStack>> researchedItems = new AtomicReference<>(new ArrayList<>());
		playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent( cap -> {
			researchedItems.set(cap.getResearchedItems().getOrDefault(stageName, new ArrayList<>()));
		});
		return researchedItems.get();
	}

    public int getRequiredExperienceCost(ItemStack input) {
    	if (containsItem(input)) {
    		for (Map.Entry<ItemStack, ResearchValues> entry : this.researchItems.entrySet()) {
    			if (entry.getKey().isItemEqual(input)) {
    				return entry.getValue().experienceCost;
				}
			}
		}

        return 0;
    }

    public int returnResearchGained(ItemStack input) {
		if (containsItem(input)) {
			for (Map.Entry<ItemStack, ResearchValues> entry : this.researchItems.entrySet()) {
				if (entry.getKey().isItemEqual(input)) {
					return entry.getValue().progressGiven;
				}
			}
		}

		return 0;
	}

	public String getDescriptiveName() {
        return this.stageName.substring(0, 1).toUpperCase() + this.stageName.substring(1) + " Research";
    }
}
