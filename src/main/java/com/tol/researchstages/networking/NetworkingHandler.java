package com.tol.researchstages.networking;

import com.tol.researchstages.capabilities.IPlayerResearch;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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

        INSTANCE.messageBuilder(ResearchTriggerPacketHandler.class, id++)
                .encoder(ResearchTriggerPacketHandler::writeToBuffer)
                .decoder(buf -> new ResearchTriggerPacketHandler(buf.readCompoundTag()))
                .consumer(ResearchTriggerPacketHandler::handle)
                .add();

        INSTANCE.messageBuilder(ResearchStudyPacketHandler.class, id++)
                .encoder(ResearchStudyPacketHandler::writeToBuffer)
                .decoder(buf -> new ResearchStudyPacketHandler(buf.readCompoundTag()))
                .consumer(ResearchStudyPacketHandler::handle)
                .add();

        INSTANCE.messageBuilder(ResearchWritingPacketHandler.class, id++)
                .encoder(ResearchWritingPacketHandler::writeToBuffer)
                .decoder(buf -> new ResearchWritingPacketHandler(buf.readCompoundTag()))
                .consumer(ResearchWritingPacketHandler::handle)
                .add();
    }

    public static void sendResearchMessageToPlayer(IPlayerResearch playerResearch, ServerPlayerEntity player, boolean playSound) {
        INSTANCE.sendTo(new ResearchUpdatePacketHandler(playerResearch, playSound), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendResearchTriggerToServer(String stageName, ItemStack item, int tableLevel) {
        INSTANCE.sendToServer(new ResearchTriggerPacketHandler(stageName, item, tableLevel));
    }

    public static void sendResearchStudyMessageToServer(String stageName, String itemType) {
        INSTANCE.sendToServer(new ResearchStudyPacketHandler(stageName, itemType));
    }

    public static void sendResearchWritingMessageToServer(String stageName, ItemStack itemStack) {
        INSTANCE.sendToServer(new ResearchWritingPacketHandler(itemStack, stageName));
    }
}
