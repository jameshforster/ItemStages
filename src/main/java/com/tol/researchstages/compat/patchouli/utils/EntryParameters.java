package com.tol.researchstages.compat.patchouli.utils;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.research.*;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EntryParameters {
    public String completedName = null;
    public String completedContent = null;
    public String incompleteName = null;
    public String incompleteContent = null;
    public String displayIcon = null;
    public ICondition displayCondition = DefaultCondition.INSTANCE;
    public String bookFileName;
    public String categoryName;
    public int sortNum = 0;

    public EntryParameters(String book, String categoryName) {
        this.bookFileName = book;
        this.categoryName = categoryName;
    }

    public String getTextContent(ClientPlayerEntity playerEntity, ResearchStage stage) {
        boolean isComplete = GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stage.stageName);
        if (!isComplete && incompleteContent != null) {
            return incompleteContent;
        } else if (completedContent != null) {
            return completedContent;
        }
        return stage.description;
    }

    public String getDocumentName(ClientPlayerEntity playerEntity, String stageName) {
        boolean isComplete = GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stageName);
        if (!isComplete && incompleteName != null) {
            return incompleteName;
        } else if (completedName != null) {
            return completedName;
        }
        return stageName;
    }

    public String getProgress(ClientPlayerEntity playerEntity, ResearchStage stage) {
        boolean isComplete = GameStageSaveHandler.getPlayerData(playerEntity.getUniqueID()).hasStage(stage.stageName);
        if (isComplete) {
            return "100%";
        }
        AtomicReference<BigDecimal> progress = new AtomicReference<>(BigDecimal.ZERO);
        playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
            progress.set(cap.getProgress(stage));
        });

        return progress.get().toString() + "%";
    }

    public void setDisplayIcon(IItemStack itemStack) {
        displayIcon = itemStack.getRegistryName().toString();
    }

    public void setDisplayCondition(String conditionType, List<String> dependencies, ResearchStage researchStage) {
        if (displayCondition instanceof DefaultCondition) {
            List<ICondition> conditions = new ArrayList<>();
            switch (conditionType) {
                case "completed": {
                    conditions.add(new CompletedCondition(researchStage.stageName));
                    break;
                }
                case "dependency": {
                    conditions.add(new DependencyCondition(dependencies));
                    break;
                }
                case "item": {
                    conditions.add(new ItemCondition(researchStage.stageName, new ArrayList<>(researchStage.researchItems.keySet())));
                    break;
                }
                default: break;
            }

            this.displayCondition = new ChainedCondition(conditions);
        }
        else if (displayCondition instanceof ChainedCondition) {
            switch (conditionType) {
                case "completed": {
                    ((ChainedCondition)this.displayCondition).updateCompletedCondition(new CompletedCondition(researchStage.stageName));
                    break;
                }
                case "dependency": {
                    ((ChainedCondition)this.displayCondition).updateDependencyCondition(new DependencyCondition(dependencies));
                    break;
                }
                case "item": {
                    ((ChainedCondition)this.displayCondition).updateItemCondition(new ItemCondition(researchStage.stageName, new ArrayList<>(researchStage.researchItems.keySet())));
                    break;
                }
                default: break;
            }
        }
    }
}
