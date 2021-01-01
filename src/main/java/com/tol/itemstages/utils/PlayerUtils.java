package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public class PlayerUtils {

	public static LazyOptional<PlayerResearch> getResearchCapability(PlayerEntity playerEntity) {
		return playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH);
	}

}
