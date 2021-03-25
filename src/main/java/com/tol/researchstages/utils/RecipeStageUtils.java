package com.tol.researchstages.utils;

import com.tol.researchstages.recipes.StagedRecipe;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeStageUtils {

    public static RecipeStageUtils INSTANCE = new RecipeStageUtils();

    public HashMap<String, List<String>> STAGED_RECIPES_NAMES = new HashMap<>();
    public HashMap<String, StagedRecipe<?>> STAGED_RECIPES = new HashMap<>();

    public boolean hasAllStages(PlayerEntity player, String resourceLocation) {
        boolean passesValidation = true;
        for (String stage : STAGED_RECIPES_NAMES.getOrDefault(resourceLocation, new ArrayList<>())) {
            if(!GameStageHelper.hasStage(player, stage)) {
                passesValidation = false;
            }
        }

        return passesValidation;
    }
}
