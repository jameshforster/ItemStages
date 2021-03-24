package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.tol.itemstages.utils.BlockStageUtils;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ActionAddBlockRestriction implements IRuntimeAction {
    private final String stage;
    private final Block block;

    public ActionAddBlockRestriction(Block block, String stage) {
        this.stage = stage;
        this.block = block;
    }

    @Override
    public void apply() {
        List<String> stages = BlockStageUtils.INSTANCE.STAGED_BLOCKS.getOrDefault(block.getRegistryName(), new ArrayList<>());
        if (!stages.contains(stage)) {
            stages.add(stage);
        }
        BlockStageUtils.INSTANCE.STAGED_BLOCKS.put(this.block.getRegistryName(), stages);
    }

    @Override
    public String describe() {
        return "Adding restriction to block: " + this.block + " for stage: " + stage;
    }
}
