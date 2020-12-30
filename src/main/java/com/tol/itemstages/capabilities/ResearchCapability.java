package com.tol.itemstages.capabilities;

import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.research.ResearchStage;
import com.tol.itemstages.utils.ResearchStageUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

			for (Map.Entry<ResearchStage, List<ItemStack>> entry : instance.researchedItems.entrySet()) {
				ListNBT items = new ListNBT();
				for (ItemStack researchedItem : entry.getValue()) {
					items.add(researchedItem.write(new CompoundNBT()));
				}
				tag.put("researched_items_" + entry.getKey().stageName, items);
			}
			return tag;
		}

		@Override
		public void readNBT(Capability<PlayerResearch> capability, PlayerResearch instance, Direction side, INBT nbt) {
			HashMap<ResearchStage, BigDecimal> playerResearch = new HashMap<>();
			HashMap<ResearchStage, List<ItemStack>> researchedItems = new HashMap<>();
			for (Map.Entry<String, ResearchStage> researchStage: ResearchStageUtils.RESEARCH_STAGES.entrySet()) {
				long progress = ((CompoundNBT) nbt).getLong("research_" + researchStage.getKey());
				INBT items = ((CompoundNBT) nbt).get("researched_items_" + researchStage.getKey());
				List<ItemStack> itemStacks = new ArrayList<>();
				if (items instanceof ListNBT) {
					for (INBT item : (ListNBT) items) {
						itemStacks.add(ItemStack.read(((CompoundNBT) item)));
					}
				}

				if (progress > 0) {
					researchedItems.put(researchStage.getValue(), itemStacks);
					playerResearch.put(researchStage.getValue(), new BigDecimal(progress));
				}
			}

			instance.setResearch(playerResearch);
			instance.setResearchedItems(researchedItems);
		}
	}
}
