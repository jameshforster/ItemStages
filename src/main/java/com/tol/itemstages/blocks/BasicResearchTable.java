package com.tol.itemstages.blocks;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.containers.ResearchTableContainer;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BasicResearchTable extends Block {

    public BasicResearchTable() {
        super(Properties.create(Material.ROCK));
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) player);
            PlayerResearch research = player.getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
			List<ResearchStage> validResearchStages = ResearchStageUtils.getOrderedValidStages(serverPlayer, player.getHeldItem(handIn), research);
			if (!validResearchStages.isEmpty()) {
				ResearchStageUtils.doResearch(serverPlayer, validResearchStages.get(0), player.getHeldItem(handIn));
			}
		}

    	if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            player.openContainer(state.getContainer(worldIn, pos));
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        if (state.getBlock() instanceof BasicResearchTable) {
            ITextComponent itextcomponent = new StringTextComponent("research_table");
            return new SimpleNamedContainerProvider((id, inventory, player) -> new ResearchTableContainer(id, worldIn, inventory, player), itextcomponent);
        } else {
            return null;
        }
    }
}
