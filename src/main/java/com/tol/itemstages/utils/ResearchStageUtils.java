package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.networking.NetworkingHandler;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    public static void doResearch(ClientPlayerEntity player, ResearchStage researchStage, ItemStack itemStack) {
        int experienceCost = researchStage.getRequiredExperienceCost(itemStack);
        BigDecimal progressValue = new BigDecimal(researchStage.returnResearchGained(itemStack));

        player.sendStatusMessage(new StringTextComponent("Adding " + progressValue + " progress towards " + researchStage.stageName + " research."), false);
        player.addExperienceLevel(-experienceCost);
        player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
            cap.updateResearch(researchStage, progressValue);
            cap.updateResearchedItem(researchStage, itemStack);
            player.sendStatusMessage(new StringTextComponent("Total progress for " + researchStage.stageName + " = " + cap.getProgress(researchStage)), false);

            boolean isComplete = cap.getResearch().getOrDefault(researchStage, new BigDecimal(0)).compareTo(new BigDecimal(100)) > -1;
            if (isComplete) {
                player.sendStatusMessage(new StringTextComponent("Research complete for " + researchStage.stageName), false);
                cap.removeResearch(researchStage);
                NetworkingHandler.sendStageUpdateToServer(researchStage.stageName);
            }

            NetworkingHandler.sendResearchMessageToServer(cap);
        });
    }

    public static List<ResearchStage> getOrderedValidStages(PlayerEntity player, ItemStack itemStack, IPlayerResearch research) {
        List<ResearchStage> orderedStages = ResearchStageUtils.getOrderedMatchingStages(itemStack);
        List<ResearchStage> validStages = new ArrayList<>();

        for (ResearchStage stage : orderedStages) {
            List<ItemStack> researchedItems = research.getResearchedItems().getOrDefault(stage, new ArrayList<>());
            if (!GameStageHelper.hasStage(player, GameStageSaveHandler.getPlayerData(player.getUniqueID()), stage.stageName) && !ItemStackUtils.containsItemStack(itemStack, researchedItems)) {
                validStages.add(stage);
            }
        }

        validStages.sort(Comparator.comparingInt(stage -> stage.getRequiredExperienceCost(itemStack)));

        return validStages;
    }
}
