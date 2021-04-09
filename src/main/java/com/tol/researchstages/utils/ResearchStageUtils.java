package com.tol.researchstages.utils;

import com.tol.researchstages.capabilities.IPlayerResearch;
import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.networking.NetworkingHandler;
import com.tol.researchstages.research.ResearchStage;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.core.jmx.Server;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
            }

        });
    }

    public static void doResearch(ServerPlayerEntity player, ResearchStage researchStage, ItemStack itemStack, int tableLevel) {
        int experienceCost = researchStage.getRequiredExperienceCost(itemStack);
        BigDecimal progressValue = new BigDecimal(researchStage.returnResearchGained(itemStack));

        if (ResearchStageUtils.isResearchableStage(player, itemStack, researchStage, tableLevel)) {
            player.addExperienceLevel(-experienceCost);
            player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
                cap.updateResearch(researchStage, progressValue);
                cap.updateResearchedItem(researchStage, itemStack);

                boolean isComplete = cap.getResearch().getOrDefault(researchStage, new BigDecimal(0)).compareTo(new BigDecimal(100)) > -1;
                if (isComplete) {
                    cap.removeResearch(researchStage);
                    GameStageHelper.addStage(player, researchStage.stageName);
                    GameStageHelper.syncPlayer(player);
                }

                NetworkingHandler.sendResearchMessageToPlayer(cap, player, true);
            });
        }
    }

    public static void studyResearch(ClientPlayerEntity player, ResearchStage researchStage, String itemType) {
        if (!GameStageHelper.hasStage(player, researchStage.stageName)) {
            int experienceCost = researchStage.getDefaultExperienceCost();
            switch (itemType) {
                case "research_scroll": {
                    if (experienceCost > 5) {
                        experienceCost = experienceCost - 5;
                    }
                    break;
                }
                case "research_book": {
                    if (experienceCost > 10) {
                        experienceCost = experienceCost / 2;
                    } else if (experienceCost > 5) {
                        experienceCost = experienceCost - 5;
                    }
                    break;
                }
            }
            player.addExperienceLevel(-experienceCost);
        }
    }

    public static int determineStudyResearchCost(ResearchStage researchStage, String itemType) {
        int experienceCost = researchStage.getDefaultExperienceCost();
        switch (itemType) {
            case "research_scroll": {
                if (experienceCost > 5) {
                    experienceCost = experienceCost - 5;
                }
                break;
            }
            case "research_book": {
                if (experienceCost > 10) {
                    experienceCost = experienceCost / 2;
                } else if (experienceCost > 5) {
                    experienceCost = experienceCost - 5;
                }
                break;
            }
        }
        return experienceCost;
    }

    public static void studyResearch(ServerPlayerEntity player, ResearchStage researchStage, String itemType) {
        if (!GameStageHelper.hasStage(player, researchStage.stageName)) {
            int experienceCost = determineStudyResearchCost(researchStage, itemType);

            if (player.experienceLevel >= experienceCost) {
                player.addExperienceLevel(-experienceCost);
                GameStageHelper.addStage(player, researchStage.stageName);
                GameStageHelper.syncPlayer(player);
                player.sendStatusMessage(new StringTextComponent("You have successfully studied the contents of this item!"), false);
            } else {
                player.sendStatusMessage(new StringTextComponent("You do not have enough experience to learn this research."), false);
            }
        } else {
            player.sendStatusMessage(new StringTextComponent("You already know this research!"), false);
        }
    }

    public static boolean isResearchableStage(PlayerEntity player, ItemStack itemStack, ResearchStage research, int tableLevel) {
        AtomicBoolean response = new AtomicBoolean(false);
        player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent( cap -> {
            List<ResearchStage> validOptions = getOrderedValidStagesForLevel(player, itemStack, cap, tableLevel);
            if (validOptions.contains(research) && research.getRequiredExperienceCost(itemStack) <= player.experienceLevel) {
                response.set(true);
            }
        });

        return response.get();
    }

    public static List<ResearchStage> getOrderedValidStages(PlayerEntity player, ItemStack itemStack, IPlayerResearch research) {
        List<ResearchStage> orderedStages = ResearchStageUtils.getOrderedMatchingStages(itemStack);
        List<ResearchStage> validStages = new ArrayList<>();

        for (ResearchStage stage : orderedStages) {
            List<ItemStack> researchedItems = research.getResearchedItems().getOrDefault(stage.stageName, new ArrayList<>());
            if (!GameStageHelper.hasStage(player, GameStageSaveHandler.getPlayerData(player.getUniqueID()), stage.stageName) && !ItemStackUtils.containsItemStack(itemStack, researchedItems)) {
                validStages.add(stage);
            }
        }

        validStages.sort(Comparator.comparingInt(stage -> stage.getRequiredExperienceCost(itemStack)));

        return validStages;
    }

    public static List<ResearchStage> getOrderedValidStagesForLevel(PlayerEntity player, ItemStack itemStack, IPlayerResearch research, int tableLevel) {
        List<ResearchStage> validStages = new ArrayList<>();
        for (ResearchStage stage: getOrderedValidStages(player,itemStack,research)) {
            if (stage.requiredLevel <= tableLevel) {
                validStages.add(stage);
            }
        }

        return validStages;
    }
}
