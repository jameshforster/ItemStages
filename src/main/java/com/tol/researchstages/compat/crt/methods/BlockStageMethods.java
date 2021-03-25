package com.tol.researchstages.compat.crt.methods;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.impl.tag.MCTag;
import com.tol.researchstages.compat.crt.actions.ActionAddBlockRestriction;
import com.tol.researchstages.utils.BlockStageUtils;
import net.minecraft.block.Block;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.ResearchStages.blocks")
public class BlockStageMethods {

    @ZenCodeType.Method
    public static void setBlockStage(String name, MCTag<Block> input) {
        for (Block block : input.getElements()) {
            CraftTweakerAPI.apply(new ActionAddBlockRestriction(block, name));
        }
    }

    @ZenCodeType.Method
    public static void setBlockStage(String name, Block input) {
        CraftTweakerAPI.apply(new ActionAddBlockRestriction(input, name));
    }

    @ZenCodeType.Method
    public static void setStagedBlockName(String name, MCTag<Block> input) {
        for (Block block : input.getElements()) {
            BlockStageUtils.INSTANCE.STAGED_BLOCK_NAMES.put(block.getRegistryName(), name);
        }
    }

    @ZenCodeType.Method
    public static void setStagedBlockName(String name, Block input) {
        BlockStageUtils.INSTANCE.STAGED_BLOCK_NAMES.put(input.getRegistryName(), name);
    }
}
