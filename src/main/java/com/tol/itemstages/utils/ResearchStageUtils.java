package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.math.BigDecimal;
import java.util.*;

public class ResearchStageUtils {
    public static HashMap<String, ResearchStage> RESEARCH_STAGES = new HashMap<>();

    public static List<ResearchStage> getOrderedMatchingStages(ItemStack itemStack) {
        List<ResearchStage> validStages = new ArrayList<>();
        for (ResearchStage researchStage : RESEARCH_STAGES.values()) {
            if (researchStage.containsItem(itemStack)) {
                validStages.add(researchStage);
            }
        }

        validStages.sort(Comparator.comparingInt(stage -> stage.getRequiredExperienceCost(itemStack)));

        return validStages;
    }

    public static void doResearch(PlayerEntity player, ResearchStage researchStage, ItemStack itemStack) {
        int experienceCost = researchStage.getRequiredExperienceCost(itemStack);
        BigDecimal progressValue = new BigDecimal(researchStage.returnResearchGained(itemStack));

        player.sendStatusMessage(new StringTextComponent("Adding " + progressValue + " progress towards " + researchStage.stageName + " research."), false);
        player.addExperienceLevel(-experienceCost);
        PlayerUtils.getResearchCapability(player).ifPresent(cap -> {
            player.sendStatusMessage(new StringTextComponent("FOUND CAPABILITY TO UPDATE"), false);
            for (Map.Entry<ResearchStage, BigDecimal> thing : cap.research.entrySet()) {
                player.sendStatusMessage(new StringTextComponent("Stage: " + thing.getKey().stageName), false);
                player.sendStatusMessage(new StringTextComponent("Progress: " + thing.getValue()), false);
            }
            cap.updateResearch(researchStage, progressValue);
            cap.updateResearchedItem(researchStage, itemStack);
            player.sendStatusMessage(new StringTextComponent("CAPABILITY UPDATED"), false);
            for (Map.Entry<ResearchStage, BigDecimal> thing : cap.research.entrySet()) {
                player.sendStatusMessage(new StringTextComponent("Stage: " + thing.getKey().stageName), false);
                player.sendStatusMessage(new StringTextComponent("Progress: " + thing.getValue()), false);
            }
        });
        boolean isComplete = PlayerUtils.getResearchCapability(player).map(cap -> cap.research.getOrDefault(researchStage, new BigDecimal(0)).compareTo(new BigDecimal(100)) > -1).orElseGet(() -> false);

        if (isComplete) {
        	ServerPlayerEntity serverPlayer = PlayerUtils.getServerPlayerFromPlayer(player);
            player.sendStatusMessage(new StringTextComponent("COMPLETED PROGRESS FOR STAGE " + researchStage.stageName), false);
            GameStageHelper.addStage(serverPlayer, researchStage.stageName);
            GameStageHelper.syncPlayer(serverPlayer);
			PlayerUtils.getResearchCapability(player).ifPresent(cap -> cap.removeResearch(researchStage));
        }
    }

    public static List<ResearchStage> getOrderedValidStages(PlayerEntity player, ItemStack itemStack, PlayerResearch research) {
        List<ResearchStage> orderedStages = ResearchStageUtils.getOrderedMatchingStages(itemStack);
        List<ResearchStage> validStages = new ArrayList<>();

        for (ResearchStage stage : orderedStages) {
            List<ItemStack> researchedItems = research.researchedItems.getOrDefault(stage, new ArrayList<>());
            if (!GameStageHelper.hasStage(player, GameStageSaveHandler.getPlayerData(player.getUniqueID()), stage.stageName) && !ItemStackUtils.containsItemStack(itemStack, researchedItems)) {
                validStages.add(stage);
            }
        }

        validStages.sort(Comparator.comparingInt(stage -> stage.getRequiredExperienceCost(itemStack)));

        return validStages;
    }
}
