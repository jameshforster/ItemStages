package com.tol.itemstages.events;

import com.tol.itemstages.stages.StageUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.Map;

public class LoaderEvents {
	@SubscribeEvent
	public void onLoadComplete (FMLLoadCompleteEvent event) {

		final long time = System.currentTimeMillis();

		for (final Map.Entry<ItemStack, String> entry : StageUtils.ITEM_STAGES.entrySet()) {

			StageUtils.SORTED_STAGES.put(entry.getValue(), entry.getKey());
			StageUtils.SORTED_ITEM_STAGES.put(entry.getKey().getItem(), new Tuple<>(entry.getKey(), entry.getValue()));
		}
	}
}
