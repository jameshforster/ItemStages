package com.tol.itemstages.registries;

import com.tol.itemstages.containers.ResearchTableContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
	public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "researchstages");

	public static RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINERS.register("research_table", () -> IForgeContainerType.create((windowId, inv, data) -> {
		World world = inv.player.getEntityWorld();
		return new ResearchTableContainer(windowId, world, inv, inv.player);
	}));

}
