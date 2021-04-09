package com.tol.researchstages.items;

import com.tol.researchstages.networking.NetworkingHandler;
import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface ResearchItem {

    default void doResearch(ClientPlayerEntity playerEntity, String stage, String itemType) {
        if (ResearchStageUtils.RESEARCH_STAGES.containsKey(stage)) {
            ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stage);
            int experienceCost = ResearchStageUtils.determineStudyResearchCost(researchStage, itemType);
            if (experienceCost > -1 && playerEntity.experienceLevel > experienceCost) {
                NetworkingHandler.sendResearchStudyMessageToServer(stage, itemType);
                ResearchStageUtils.studyResearch(playerEntity, ResearchStageUtils.RESEARCH_STAGES.get(stage), stage);
            }
        }
    }
}
