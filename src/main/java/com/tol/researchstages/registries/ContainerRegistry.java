package com.tol.researchstages.registries;

import com.tol.researchstages.blocks.ResearchTable;
import com.tol.researchstages.containers.ResearchTableContainer;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "researchstages");

	public static RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINERS.register("research_table", () -> IForgeContainerType.create((windowId, inv, data) -> {
		World world = inv.player.getEntityWorld();
		BlockPos blockPos = data.readBlockPos();
		Block block = world.getBlockState(blockPos).getBlock();
		int level = 0;
		if (block instanceof ResearchTable) {
			level = ((ResearchTable) block).getLevel();
		}

		return new ResearchTableContainer(windowId, world, inv, inv.player, level);
	}));

}
