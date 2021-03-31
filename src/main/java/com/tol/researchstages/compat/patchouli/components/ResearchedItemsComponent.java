package com.tol.researchstages.compat.patchouli.components;

import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ResearchedItemsComponent extends ResearchItemComponent {
    public final String heading = "Researched Items";

    @Override
    public List<ItemStack> getItems(String stage) {
        return ResearchStageUtils.RESEARCH_STAGES.get(stage).getResearchedItems(Minecraft.getInstance().player);
    }
}
