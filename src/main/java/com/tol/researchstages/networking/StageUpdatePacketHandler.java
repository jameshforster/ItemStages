package com.tol.researchstages.networking;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StageUpdatePacketHandler {

    public String stageName;

    public StageUpdatePacketHandler(String name) {
        this.stageName = name;
    }

    public void writeToBuffer(PacketBuffer pkt) {
        pkt.writeString(stageName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            GameStageHelper.addStage(sender, stageName);
            GameStageHelper.syncPlayer(sender);
        });
        return true;
    }
}
