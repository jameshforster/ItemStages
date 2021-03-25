package com.tol.researchstages.research;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.util.List;

public class DependencyCondition implements ICondition {
    List<String> dependencyNames;

    public DependencyCondition(List<String> dependencyNames) {
        this.dependencyNames = dependencyNames;
    }

    @Override
    public boolean checkCondition(ClientPlayerEntity player) {
        boolean hasAllStages = true;
        for (String dependency: dependencyNames) {
            if (!GameStageSaveHandler.getPlayerData(player.getUniqueID()).hasStage(dependency)) {
                hasAllStages = false;
            }
        }
        return hasAllStages;
    }
}
