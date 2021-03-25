package com.tol.itemstages.compat.patchouli.processors;

import com.tol.itemstages.blocks.ResearchTable;
import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ItemStackUtils;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResearchComponentProcessor implements IComponentProcessor {
    private ResearchStage researchStage;
    private ClientPlayerEntity playerEntity;

    @Override
    public void setup(IVariableProvider variables) {
        String researchName = variables.get("stageName").asString();
        String playerId = variables.get("playerId").asString();

        playerEntity = (ClientPlayerEntity) Minecraft.getInstance().world.getPlayerByUuid(UUID.fromString(playerId));
        researchStage = ResearchStageUtils.RESEARCH_STAGES.get(researchName);
    }

    @Override
    public IVariable process(String key) {
        switch (key) {
            case "stageName": return IVariable.from(researchStage.stageName);
            case "content": return IVariable.from(researchStage.getTextContent(playerEntity));
            case "remainingItems": return IVariable.from(researchStage.unresearchedItems(playerEntity).toArray());
            case "researchedItems": {
                List<ItemStack> items = playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH).map( cap ->
                    cap.getResearchedItems().getOrDefault(researchStage, new ArrayList<>())
                ).orElseGet(ArrayList::new);
                return IVariable.from(items.toArray());
            }
        }
        return null;
    }
}
