package com.tol.itemstages.containers;

import net.minecraft.block.CraftingTableBlock;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ContainerRegistry {
    public static ContainerType<?> RESEARCH_TABLE = null;

    public static void registerContainer(@Nonnull RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
        RESEARCH_TABLE = registry.register(Registry.MENU, RESEARCH_TABLE.setRegistryName("research_table"));
    }
}
