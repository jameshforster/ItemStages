package com.tol.itemstages.capabilities;

import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Map;

public class ResearchCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
	private final PlayerResearch base = new PlayerResearch();
	private final LazyOptional<PlayerResearch> baseOptional = LazyOptional.of(() -> base);

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ResearchCapability.PLAYER_RESEARCH) {
			return baseOptional.cast();
		}

		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		LogManager.getLogger().info("[RESEARCHSTAGES] Call to save nbt data for capability.");
		if (ResearchCapability.PLAYER_RESEARCH == null) {
			LogManager.getLogger().info("[RESEARCHSTAGES] Null instance found.");
			return new CompoundNBT();
		} else {
			LogManager.getLogger().info("[RESEARCHSTAGES] Non null instance found.");
			for (Map.Entry<ResearchStage, BigDecimal> entry: base.getResearch().entrySet()) {
				LogManager.getLogger().info("[RESEARCHSTAGES] Stage " + entry.getKey().stageName + " with progress: " + entry.getValue());
			}
			return (CompoundNBT) ResearchCapability.PLAYER_RESEARCH.writeNBT(base, null);
		}
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		LogManager.getLogger().info("[RESEARCHSTAGES] Call to read nbt data for capability.");
		if (ResearchCapability.PLAYER_RESEARCH != null) {
			LogManager.getLogger().info("[RESEARCHSTAGES] Non null instance found.");
			ResearchCapability.PLAYER_RESEARCH.readNBT(base, null, nbt);
		}
	}
}
