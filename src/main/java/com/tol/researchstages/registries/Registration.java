package com.tol.researchstages.registries;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Registration {

	public static void init() {
		BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ContainerRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
