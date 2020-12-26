package com.tol.itemstages.containers;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.util.registry.Registry;

public class ContainerTypeHandler {

	public static final ContainerType<ResearchTableContainer> RESEARCH_TABLE = register("research_table", ResearchTableContainer::new);

	private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
		return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
	}
}
