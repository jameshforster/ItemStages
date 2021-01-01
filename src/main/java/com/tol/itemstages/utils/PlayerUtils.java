package com.tol.itemstages.utils;

import com.tol.itemstages.capabilities.IPlayerResearch;
import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerUtils {

	public static LazyOptional<IPlayerResearch> getResearchCapability(PlayerEntity playerEntity) {
		return playerEntity.getCapability(ResearchCapability.PLAYER_RESEARCH);
	}

}
