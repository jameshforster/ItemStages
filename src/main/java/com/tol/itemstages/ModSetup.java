package com.tol.itemstages;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.networking.NetworkingHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "researchstages", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
        ResearchCapability.register();
        NetworkingHandler.registerMessages();
    }
}