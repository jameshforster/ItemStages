package com.tol.itemstages;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.capabilities.ResearchCapabilityProvider;
import com.tol.itemstages.events.ClientEvents;
import com.tol.itemstages.events.LoaderEvents;
import com.tol.itemstages.events.PlayerEvents;
import com.tol.itemstages.gui.ResearchTableGui;
import com.tol.itemstages.networking.NetworkingHandler;
import com.tol.itemstages.registries.ContainerRegistry;
import com.tol.itemstages.registries.Registration;
import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("researchstages")
public class ItemStages
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ItemStages() {
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);

		Registration.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new LoaderEvents());
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
		ScreenManager.registerFactory(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableGui::new);
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

	@SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    	if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation("researchstages", "research"), new ResearchCapabilityProvider());
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(PlayerEvent.Clone event) {
    	if (event.isWasDeath()) {
    		event.getOriginal().getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
    			event.getEntity().getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(newCap -> {
					newCap.setResearch(cap.research);
					newCap.setResearchedItems(cap.researchedItems);
				});
			});
		}
	}
}
