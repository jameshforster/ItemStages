package com.tol.researchstages.research;

import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.utils.ItemStackUtils;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemCondition implements ICondition {

    private final List<ItemStack> researchItems;
    private final String researchName;

    public ItemCondition(String researchName, List<ItemStack> researchItems) {
        this.researchName = researchName;
        this.researchItems = researchItems;
    }

    @Override
    public boolean checkCondition(ClientPlayerEntity player) {
        AtomicBoolean foundItem = new AtomicBoolean(false);
        for (ItemStack item: researchItems) {
            player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
                    if (ItemStackUtils.containsItemStack(item, cap.getResearchedItems().getOrDefault(researchName, new ArrayList<>()))) {
                        foundItem.set(true);
                    }
                }
            );
        }
        return foundItem.get();
    }
}
