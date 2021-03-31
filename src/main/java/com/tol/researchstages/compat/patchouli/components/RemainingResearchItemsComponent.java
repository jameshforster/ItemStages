package com.tol.researchstages.compat.patchouli.components;

import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RemainingResearchItemsComponent extends ResearchItemComponent {

    @Override
    public List<ItemStack> getItems(String stage) {
        return ResearchStageUtils.RESEARCH_STAGES.get(stage).getUnresearchedItems(Minecraft.getInstance().player);
    }
}
