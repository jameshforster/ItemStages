package com.tol.itemstages.networking;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ResearchUpdatePacketHandler {

    private PlayerResearch playerResearch;

    public ResearchUpdatePacketHandler(PlayerResearch playerResearch) {
        this.playerResearch = playerResearch;
    }

    public ResearchUpdatePacketHandler(CompoundNBT compoundNBT) {
        this.playerResearch = new PlayerResearch(compoundNBT);
    }

    public void writeToBuffer(PacketBuffer pkt) {
        pkt.writeCompoundTag(this.toNbt());
    }

    private CompoundNBT toNbt() {
        CompoundNBT tag = new CompoundNBT();

        for (Map.Entry<ResearchStage, BigDecimal> entry: this.playerResearch.research.entrySet()) {
            tag.putLong("research_" + entry.getKey().stageName, entry.getValue().longValue());
        }

        for (Map.Entry<ResearchStage, List<ItemStack>> entry : this.playerResearch.researchedItems.entrySet()) {
            ListNBT items = new ListNBT();
            for (ItemStack researchedItem : entry.getValue()) {
                items.add(researchedItem.write(new CompoundNBT()));
            }
            tag.put("researched_items_" + entry.getKey().stageName, items);
        }
        return tag;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NetworkDirection direction = ctx.get().getDirection();
            if (direction == NetworkDirection.PLAY_TO_CLIENT) {
                ctx.get().getSender().sendStatusMessage(new StringTextComponent("CLIENT RECEIVED MESSAGE FOR RESEARCH UPDATE"), false);
                Minecraft.getInstance().player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
                    cap.research = this.playerResearch.research;
                    cap.researchedItems = this.playerResearch.researchedItems;
                });
            } else {
                ctx.get().getSender().sendStatusMessage(new StringTextComponent("SERVER RECEIVED MESSAGE FOR RESEARCH UPDATE"), false);
                ServerPlayerEntity sender = ctx.get().getSender();
                sender.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent( cap -> {
                    cap.research = this.playerResearch.research;
                    cap.researchedItems = this.playerResearch.researchedItems;
                });
            }
        });
        return true;
    }
}
