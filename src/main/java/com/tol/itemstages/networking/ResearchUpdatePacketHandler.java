package com.tol.itemstages.networking;

import com.tol.itemstages.capabilities.IPlayerResearch;
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
import org.apache.logging.log4j.LogManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ResearchUpdatePacketHandler {

    private IPlayerResearch playerResearch;

    public ResearchUpdatePacketHandler(IPlayerResearch playerResearch) {
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

        for (Map.Entry<ResearchStage, BigDecimal> entry: this.playerResearch.getResearch().entrySet()) {
            tag.putLong("research_" + entry.getKey().stageName, entry.getValue().longValue());
        }

        for (Map.Entry<ResearchStage, List<ItemStack>> entry : this.playerResearch.getResearchedItems().entrySet()) {
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
			LogManager.getLogger().info("[RESEARCHSTAGES] Logged message with research data.");
			for (Map.Entry<ResearchStage, BigDecimal> entry: this.playerResearch.getResearch().entrySet()) {
				LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
			}
            NetworkDirection direction = ctx.get().getDirection();
            if (direction == NetworkDirection.PLAY_TO_CLIENT) {
				LogManager.getLogger().info("[RESEARCHSTAGES] Logged message to client from server.");
                Minecraft.getInstance().player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
					LogManager.getLogger().info("[RESEARCHSTAGES] Updating client capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry: cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
					cap.setResearch(this.playerResearch.getResearch());
					cap.setResearchedItems(this.playerResearch.getResearchedItems());
					LogManager.getLogger().info("[RESEARCHSTAGES] Updated client capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry: cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
                });
            } else {
				LogManager.getLogger().info("[RESEARCHSTAGES] Logged message to server from client.");
                ServerPlayerEntity sender = ctx.get().getSender();
                sender.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent( cap -> {
					LogManager.getLogger().info("[RESEARCHSTAGES] Updating server capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry: cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
					cap.setResearch(this.playerResearch.getResearch());
					cap.setResearchedItems(this.playerResearch.getResearchedItems());
					LogManager.getLogger().info("[RESEARCHSTAGES] Updated server capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry: cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
                });
            }
        });
        return true;
    }
}
