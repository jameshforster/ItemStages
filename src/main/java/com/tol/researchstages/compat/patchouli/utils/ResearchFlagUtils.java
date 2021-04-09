package com.tol.researchstages.compat.patchouli.utils;

import com.tol.researchstages.research.DefaultCondition;
import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.HashMap;
import java.util.List;

public class ResearchFlagUtils {
    public static HashMap<String, List<String>> STAGED_CATEGORIES = new HashMap<>();

    public static void setStagedCategoryFlags(ClientPlayerEntity player) {
        for (String category : STAGED_CATEGORIES.keySet()) {
            boolean hasAllStages = true;
            for (String stage : STAGED_CATEGORIES.get(category)) {
                if (!GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(stage)) {
                    hasAllStages = false;
                }
            }

            PatchouliAPI.get().setConfigFlag(category, hasAllStages);
        }
    }

    public static void setEntryFlags(ClientPlayerEntity player) {
        for (ResearchStage stage : ResearchStageUtils.RESEARCH_STAGES.values()) {
            if (!(stage.bookParams.displayCondition instanceof DefaultCondition)) {
                PatchouliAPI.get().setConfigFlag(stage.stageName, stage.bookParams.displayCondition.checkCondition(player));
            }
        }
    }
}
