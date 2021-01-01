package com.tol.itemstages.networking;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkingHandler {
    private static SimpleChannel INSTANCE;
    private static int id = 0;
    private static final  String PROTOCOL_VERSION = "1";

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("researchstages", "research_networking"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.messageBuilder(ResearchUpdatePacketHandler.class, id++)
                .encoder(ResearchUpdatePacketHandler::writeToBuffer)
                .decoder(buf -> new ResearchUpdatePacketHandler(buf.readCompoundTag()))
                .consumer(ResearchUpdatePacketHandler::handle)
                .add();

        INSTANCE.messageBuilder(StageUpdatePacketHandler.class, id++)
                .encoder(StageUpdatePacketHandler::writeToBuffer)
                .decoder(buf -> new StageUpdatePacketHandler(buf.readString()))
                .consumer(StageUpdatePacketHandler::handle)
                .add();
    }


    public static void sendResearchMessageToServer(IPlayerResearch playerResearch) {
        INSTANCE.sendToServer(new ResearchUpdatePacketHandler(playerResearch));
    }

    public static void sendResearchMessageToPlayer(IPlayerResearch playerResearch, ServerPlayerEntity player) {
        INSTANCE.sendTo(new ResearchUpdatePacketHandler(playerResearch), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendStageUpdateToServer(String stageName) {
        INSTANCE.sendToServer(new StageUpdatePacketHandler(stageName));
    }
}
