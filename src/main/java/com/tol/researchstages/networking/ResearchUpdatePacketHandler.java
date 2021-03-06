package com.tol.researchstages.networking;

import com.tol.researchstages.capabilities.IPlayerResearch;
import com.tol.researchstages.capabilities.ResearchCapability;
import com.tol.researchstages.compat.patchouli.utils.BookReloadUtils;
import com.tol.researchstages.events.ResearchEvents;
import com.tol.researchstages.research.PlayerResearch;
import com.tol.researchstages.research.ResearchStage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ResearchUpdatePacketHandler {

	private IPlayerResearch playerResearch;
	private boolean playSound;

	public ResearchUpdatePacketHandler(IPlayerResearch playerResearch, boolean playSound) {
		this.playerResearch = playerResearch;
		this.playSound = playSound;
	}

	public ResearchUpdatePacketHandler(CompoundNBT compoundNBT) {
		this.playerResearch = new PlayerResearch(compoundNBT);
		this.playSound = compoundNBT.getBoolean("playSound");
	}

	public void writeToBuffer(PacketBuffer pkt) {
		pkt.writeCompoundTag(this.toNbt());
	}

	private CompoundNBT toNbt() {
		CompoundNBT tag = new CompoundNBT();

		tag.putBoolean("playSound", playSound);

		for (Map.Entry<ResearchStage, BigDecimal> entry : this.playerResearch.getResearch().entrySet()) {
			tag.putLong("research_" + entry.getKey().stageName, entry.getValue().longValue());
		}

		for (Map.Entry<String, List<ItemStack>> entry : this.playerResearch.getResearchedItems().entrySet()) {
			ListNBT items = new ListNBT();
			for (ItemStack researchedItem : entry.getValue()) {
				items.add(researchedItem.write(new CompoundNBT()));
			}
			tag.put("researched_items_" + entry.getKey(), items);
		}
		return tag;
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LogManager.getLogger().info("[RESEARCHSTAGES] Logged message with research data.");
			for (Map.Entry<ResearchStage, BigDecimal> entry : this.playerResearch.getResearch().entrySet()) {
				LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
			}
			NetworkDirection direction = ctx.get().getDirection();
			if (direction == NetworkDirection.PLAY_TO_CLIENT) {
				LogManager.getLogger().info("[RESEARCHSTAGES] Logged message to client from server.");
				Minecraft.getInstance().player.getCapability(ResearchCapability.PLAYER_RESEARCH).ifPresent(cap -> {
					LogManager.getLogger().info("[RESEARCHSTAGES] Updating client capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry : cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
					cap.setResearch(this.playerResearch.getResearch());
					cap.setResearchedItems(this.playerResearch.getResearchedItems());
					LogManager.getLogger().info("[RESEARCHSTAGES] Updated client capability.");
					for (Map.Entry<ResearchStage, BigDecimal> entry : cap.getResearch().entrySet()) {
						LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress " + entry.getValue());
					}
				});
				if (playSound) {
					Minecraft.getInstance().player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F,(new Random().nextFloat() - new Random().nextFloat()) * 0.35F + 0.9F);
				}
				BookReloadUtils.patchouliUpdateConditional(Minecraft.getInstance().player);
			}
		});
		return true;
	}
}
