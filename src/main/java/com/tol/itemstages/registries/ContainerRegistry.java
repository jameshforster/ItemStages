package com.tol.itemstages.registries;

import com.tol.itemstages.containers.ResearchTableContainer;
import net.minecraft.block.Block;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ContainerRegistry {
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "research_stages");

	public static RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINERS.register("research_table", () -> IForgeContainerType.create((windowId, inv, data) -> {
		World world = inv.player.getEntityWorld();
		return new ResearchTableContainer(windowId, world, inv, inv.player);
	}));

}
