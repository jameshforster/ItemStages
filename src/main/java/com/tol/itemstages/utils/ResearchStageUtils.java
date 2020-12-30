package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.core.jmx.Server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchStageUtils {
    public static HashMap<String, ResearchStage> RESEARCH_STAGES = new HashMap<>();

    public List<ResearchStage> getOrderedMatchingStages(ItemStack itemStack) {
    	List<ResearchStage> validStages = new ArrayList<>();
    	for (ResearchStage researchStage : RESEARCH_STAGES.values()) {
    		if (researchStage.containsItem(itemStack)) {
    			validStages.add(researchStage);
			}
		}

    	validStages.sort((stage1, stage2) -> Integer.compare(stage2.getRequiredExperienceCost(itemStack), stage1.getRequiredExperienceCost(itemStack)));

    	return validStages;
	}

	public static void doResearch(ServerPlayerEntity player, ResearchStage researchStage, ItemStack itemStack) {
    	int experienceCost = researchStage.getRequiredExperienceCost(itemStack);
    	BigDecimal progressValue = new BigDecimal(researchStage.returnResearchGained(itemStack));

    	player.sendStatusMessage(new StringTextComponent("BASIC ITEM MATCH: " + researchStage.containsBasicItem(itemStack)), false);
    	player.sendStatusMessage(new StringTextComponent("ADVANCED ITEM MATCH: " + researchStage.containsAdvancedItem(itemStack)), false);
    	player.sendStatusMessage(new StringTextComponent("ALL ITEM MATCH: " + researchStage.containsItem(itemStack)), false);

		player.sendStatusMessage(new StringTextComponent("Adding " + progressValue + " progress towards " + researchStage.stageName + " research."), false);
		player.addExperienceLevel(-experienceCost);
    	player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
			player.sendStatusMessage(new StringTextComponent("FOUND CAPABILITY TO UPDATE"), false);
			for (Map.Entry<ResearchStage, BigDecimal> thing: cap.research.entrySet()) {
				player.sendStatusMessage(new StringTextComponent("Stage: " + thing.getKey().stageName), false);
				player.sendStatusMessage(new StringTextComponent("Progress: " + thing.getValue()), false);
			}
			cap.updateResearch(researchStage, progressValue);
			player.sendStatusMessage(new StringTextComponent("CAPABILITY UPDATED"), false);
			for (Map.Entry<ResearchStage, BigDecimal> thing: cap.research.entrySet()) {
				player.sendStatusMessage(new StringTextComponent("Stage: " + thing.getKey().stageName), false);
				player.sendStatusMessage(new StringTextComponent("Progress: " + thing.getValue()), false);
			}
		});
    	boolean isComplete = player.getCapability(ResearchCapability.PLAYER_RESEARCH).map(cap -> cap.research.getOrDefault(researchStage, new BigDecimal(0)).compareTo(new BigDecimal(100)) > -1).orElseGet(() -> false);

    	if (isComplete) {
    		player.sendStatusMessage(new StringTextComponent("COMPLETED PROGRESS FOR STAGE " + researchStage.stageName), false);
			GameStageHelper.addStage(player, researchStage.stageName);
			GameStageHelper.syncPlayer(player);
			player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> cap.removeResearch(researchStage));
		}
	}
}
