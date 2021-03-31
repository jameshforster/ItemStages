package com.tol.researchstages.research;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.compat.patchouli.utils.EntryWriterUtil;
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
    public String completedName = null;
    public String completedContent = null;
    public String incompleteName = null;
    public String incompleteContent = null;
    public String displayIcon = null;
    public ICondition displayCondition = DefaultCondition.INSTANCE;
    public HashMap<ItemStack, ResearchValues> researchItems = new HashMap<>();
    private final ResearchValues basicResearchValuesDefault;
    private final ResearchValues advancedResearchValuesDefault;
    public int requiredLevel = 0;
    public final String path;

    public ResearchStage(String stageName, int defaultExperienceCost, int defaultResearchValue) {
		this.stageName = stageName;

		int calculatedExpCost = 0;
		if (defaultExperienceCost > 5) {
			calculatedExpCost = defaultExperienceCost - 5;
		}

		basicResearchValuesDefault = new ResearchValues(defaultExperienceCost, defaultResearchValue);
		advancedResearchValuesDefault = new ResearchValues(calculatedExpCost, defaultResearchValue * 2);
		path = "/patchouli_books/research_compendium/en_us/entries/" + stageName + ".json";
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
		path = "/patchouli_books/research_compendium/en_us/entries/" + stageName + ".json";
    }

    public void addBasicItem(ItemStack input) {
		this.researchItems.put(input, basicResearchValuesDefault);
    }

    public void addAdvancedItem(ItemStack input) {
		this.researchItems.put(input, advancedResearchValuesDefault);
    }

    public void addResearchItem(ItemStack input, int experienceCost, int progressValue) {
    	this.researchItems.put(input, new ResearchValues(experienceCost, progressValue));
	}

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean containsItem(ItemStack input) {
		return ItemStackUtils.containsItemStack(input, this.researchItems.keySet());
    }

    public List<ItemStack> getUnresearchedItems(ClientPlayerEntity playerEntity) {
    	if (GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName) || !displayCondition.checkCondition(playerEntity)) {
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

    public String getTextContent(ClientPlayerEntity playerEntity) {
    	boolean isComplete = GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName);
    	if (displayCondition.checkCondition(playerEntity)) {
			if (!isComplete && incompleteContent != null) {
				return incompleteContent;
			} else if (completedContent != null) {
				return completedContent;
			}
			return description;
		}

    	return "You do not yet understand the complexities of this research.";
	}

	public String getDocumentName(ClientPlayerEntity playerEntity) {
		boolean isComplete = GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName);
		if (displayCondition.checkCondition(playerEntity)) {
			if (!isComplete && incompleteName != null) {
				return incompleteName;
			} else if (completedName != null) {
				return completedName;
			}
			return stageName;
		}

		return "Unknown research";
	}

	public void setDisplayIcon(IItemStack itemStack) {
    	displayIcon = itemStack.getRegistryName().toString();
	}

	public void setDisplayCondition(String conditionType, List<String> dependencies) {
    	switch (conditionType) {
			case "completed": this.displayCondition = new CompletedCondition(this.stageName);
			case "dependency": this.displayCondition = new DependencyCondition(dependencies);
			case "item": this.displayCondition = new ItemCondition(this.stageName, new ArrayList<>(this.researchItems.keySet()));
			default: break;
		}
	}
}
