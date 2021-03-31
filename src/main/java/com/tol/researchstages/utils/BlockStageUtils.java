package com.tol.researchstages.utils;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockStageUtils {

    public static BlockStageUtils INSTANCE = new BlockStageUtils();

    public HashMap<ResourceLocation, List<String>> STAGED_BLOCKS = new HashMap<>();
    public HashMap<ResourceLocation, String> STAGED_BLOCK_NAMES = new HashMap<>();

    public boolean hasAllStages(PlayerEntity player, ResourceLocation resourceLocation) {
        boolean passesValidation = true;
        for (String stage : STAGED_BLOCKS.getOrDefault(resourceLocation, new ArrayList<>())) {
            if(!GameStageHelper.hasStage(player, stage)) {
                passesValidation = false;
            }
        }

        return passesValidation;
    }
}
