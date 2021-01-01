package com.tol.itemstages;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.capabilities.ResearchCapabilityProvider;
import com.tol.itemstages.networking.NetworkingHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(modid = "researchstages", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
		ResearchCapability.register();
    	NetworkingHandler.registerMessages();
		MinecraftForge.EVENT_BUS.addListener(ModSetup::attachCapabilities);
		LogManager.getLogger().info("[RESEARCHSTAGES] Checking capability registration: " + ResearchCapability.PLAYER_RESEARCH != null);
    }

	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation("researchstages", "research"), new ResearchCapabilityProvider());
		}
	}
}
