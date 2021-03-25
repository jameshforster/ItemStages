package com.tol.researchstages.compat.crt.methods;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages.book")
public class ResearchBookMethods {
    @ZenCodeType.Method
    public static void setResearchStageBookDescription(String stageName, String content) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.completedContent = content;
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageBookIncompleteDescription(String stageName, String content) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.incompleteContent = content;
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageBookTitle(String stageName, String title) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.completedName = title;
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageBookIncompleteTitle(String stageName, String title) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.incompleteName = title;
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageCondition(String stageName, String condition) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null && !condition.equals("dependency")) {
            researchStage.bookParams.setDisplayCondition(condition, new ArrayList<>(), researchStage);
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageCondition(String stageName, String condition, String[] stages) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.setDisplayCondition(condition, Arrays.asList(stages.clone()), researchStage);
        }
    }

    @ZenCodeType.Method
    public static void setResearchStageIcon(String stageName, IItemStack displayItem) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.setDisplayIcon(displayItem);
        }
    }

    @ZenCodeType.Method
    public static void setResearchBook(String stageName, String bookFileName) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.bookFileName = bookFileName;
        }
    }

    @ZenCodeType.Method
    public static void setResearchCategory(String stageName, String categoryName) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.categoryName = categoryName;
        }
    }

    @ZenCodeType.Method
    public static void setResearchCategoryNumber(String stageName, int number) {
        ResearchStage researchStage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
        if (researchStage != null) {
            researchStage.bookParams.sortNum = number;
        }
    }
}
