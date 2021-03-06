package com.tol.researchstages;

import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.events.ClientEvents;
import com.tol.researchstages.events.LoaderEvents;
import com.tol.researchstages.events.PlayerEvents;
import com.tol.researchstages.gui.ResearchTableGui;
import com.tol.researchstages.registries.ContainerRegistry;
import com.tol.researchstages.registries.Registration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("researchstages")
public class ResearchStages {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "researchstages";

	public ResearchStages() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigurationHandler.SERVER_CONFIG);
		ConfigurationHandler.loadConfig(ConfigurationHandler.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
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

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m -> m.getMessageSupplier().get()).
				collect(Collectors.toList()));
	}

	@SubscribeEvent
	public void onPlayerDeath(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			event.getOriginal().getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
				event.getEntity().getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(newCap -> {
					newCap.setResearch(cap.getResearch());
					newCap.setResearchedItems(cap.getResearchedItems());
				});
			});
		}
	}
}
