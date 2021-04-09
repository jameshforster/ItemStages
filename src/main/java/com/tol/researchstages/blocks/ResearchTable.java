package com.tol.researchstages.blocks;

import com.tol.researchstages.containers.ResearchTableContainer;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class ResearchTable extends DirectionalBlock {
    private final int level;

    public ResearchTable(int level, AbstractBlock.Properties properties) {
        super(properties);
        this.level = level;
        this.setDefaultState(
                this.getStateContainer().getBaseState()
                        .with(FACING, Direction.NORTH)
        );
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            player.openContainer(state.getContainer(worldIn, pos));
            return ActionResultType.SUCCESS;
        } else {
            NetworkHooks.openGui((ServerPlayerEntity) player, state.getContainer(worldIn, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        if (state.getBlock() instanceof ResearchTable) {
            ITextComponent itextcomponent = new StringTextComponent("research_table");
            return new SimpleNamedContainerProvider((id, inventory, player) -> new ResearchTableContainer(id, worldIn, inventory, player, this.level), itextcomponent);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        super.fillStateContainer(builder);
    }

    public int getLevel() {
        return level;
    }
}
