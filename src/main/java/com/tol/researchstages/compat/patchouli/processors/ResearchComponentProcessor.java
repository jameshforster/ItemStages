package com.tol.researchstages.compat.patchouli.processors;

import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;


public class ResearchComponentProcessor implements IComponentProcessor {
    private ResearchStage researchStage;
    private ClientPlayerEntity playerEntity;

    @Override
    public void setup(IVariableProvider variables) {
        String researchName = variables.get("stageName").asString();

        playerEntity = Minecraft.getInstance().player;
        researchStage = ResearchStageUtils.RESEARCH_STAGES.get(researchName);
    }

    @Override
    public IVariable process(String key) {
        switch (key) {
            case "stageName": return IVariable.wrap(researchStage.stageName);
            case "researchTitle": return IVariable.wrap(researchStage.getDocumentName(playerEntity));
            case "content": return IVariable.wrap(researchStage.getTextContent(playerEntity));
        }
        return null;
    }
}
