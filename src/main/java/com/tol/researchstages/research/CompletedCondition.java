package com.tol.researchstages.research;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class CompletedCondition implements ICondition {

    private final String stageName;

    public CompletedCondition(String stageName) {
        this.stageName = stageName;
    }

    @Override
    public boolean checkCondition(ClientPlayerEntity player) {
        return GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(stageName);
    }
}
