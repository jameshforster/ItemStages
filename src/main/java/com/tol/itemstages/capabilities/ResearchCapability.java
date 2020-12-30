package com.tol.itemstages.capabilities;

import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ResearchCapability {

	@CapabilityInject(PlayerResearch.class)
	public static Capability<PlayerResearch> PLAYER_RESEARCH = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(PlayerResearch.class, new Storage(), PlayerResearch::new);
	}

	public static class Storage implements Capability.IStorage<PlayerResearch> {

		@Nullable
		@Override
		public INBT writeNBT(Capability<PlayerResearch> capability, PlayerResearch instance, Direction side) {
			CompoundNBT tag = new CompoundNBT();
			for (Map.Entry<ResearchStage, BigDecimal> entry: instance.research.entrySet()) {
				tag.putLong("research_" + entry.getKey().stageName, entry.getValue().longValue());
			}
			return tag;
		}

		@Override
		public void readNBT(Capability<PlayerResearch> capability, PlayerResearch instance, Direction side, INBT nbt) {
			HashMap<ResearchStage, BigDecimal> playerResearch = new HashMap<>();
			for (Map.Entry<String, ResearchStage> researchStage: ResearchStageUtils.RESEARCH_STAGES.entrySet()) {
				long progress = ((CompoundNBT) nbt).getLong("research_" + researchStage.getKey());

				if (progress > 0) {
					playerResearch.put(researchStage.getValue(), new BigDecimal(progress));
				}
			}

			instance.setResearch(playerResearch);
		}
	}
}
