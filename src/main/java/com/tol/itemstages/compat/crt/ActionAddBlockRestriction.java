package com.tol.itemstages.compat.crt;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.impl.blocks.MCBlock;
import com.tol.itemstages.utils.BlockStageUtils;

import java.util.ArrayList;
import java.util.List;

public class ActionAddBlockRestriction implements IRuntimeAction {
    private final String stage;
    private final MCBlock block;

    public ActionAddBlockRestriction(MCBlock block, String stage) {
        this.stage = stage;
        this.block = block;
    }

    @Override
    public void apply() {
        List<String> stages = BlockStageUtils.INSTANCE.STAGED_BLOCKS.getOrDefault(block.getInternal().getRegistryName(), new ArrayList<>());

        BlockStageUtils.INSTANCE.STAGED_BLOCKS.put(block.getInternal().getRegistryName(), stages);
    }

    @Override
    public String describe() {
        return null;
    }
}
