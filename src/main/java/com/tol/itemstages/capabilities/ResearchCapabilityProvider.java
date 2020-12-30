package com.tol.itemstages.capabilities;

import com.tol.itemstages.research.PlayerResearch;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
		if (ResearchCapability.PLAYER_RESEARCH == null) {
			return new CompoundNBT();
		} else {
			return (CompoundNBT) ResearchCapability.PLAYER_RESEARCH.writeNBT(base, null);
		}
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {

	}
}
