package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public class PlayerUtils {

	public static ServerPlayerEntity getServerPlayerFromPlayer(PlayerEntity playerEntity) {
		if (playerEntity instanceof ServerPlayerEntity) {
			return (ServerPlayerEntity) playerEntity;
		}

		return playerEntity.getServer().getPlayerList().getPlayerByUUID(playerEntity.getUniqueID());
	}

	public static LazyOptional<PlayerResearch> getResearchCapability(PlayerEntity playerEntity) {
		return getServerPlayerFromPlayer(playerEntity).getCapability(ResearchCapability.PLAYER_RESEARCH);
	}

}
