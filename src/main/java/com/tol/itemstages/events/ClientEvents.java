package com.tol.itemstages.events;

import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.StageUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.Iterator;

public class ClientEvents {

	private static final String TRANSLATE_DESCRIPTION = "tooltip.itemstages.description";
	private static final String TRANSLATE_ENCHANT_DESCRIPTION = "tooltip.itemstages.enchant";
	private static final String TRANSLATE_INFO = "tooltip.itemstages.info";
	private static final String TRANSLATE_STAGE = "tooltip.itemstages.stage";
	private static final String TRANSLATE_DROP = "message.itemstages.drop";
	private static final String TRANSLATE_ATTACK = "message.itemstages.attack";

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTooltip (ItemTooltipEvent event) {

		final ClientPlayerEntity player = PlayerUtils.getClientPlayer();

		if (player != null) {

			final String itemsStage = StageUtils.getStage(event.getItemStack());
			final String enchantStage = StageUtils.getEnchantStage(event.getItemStack());

			// Add message to items when the player doesn't have access to it.
			if (itemsStage != null && !GameStageHelper.hasStage(player, itemsStage) && ConfigurationHandler.changeRestrictionTooltip) {

				event.getToolTip().clear();
				event.getToolTip().add(new StringTextComponent(TextFormatting.WHITE + StageUtils.getUnfamiliarName(event.getItemStack())));
				event.getToolTip().add(new StringTextComponent(" "));
				event.getToolTip().add(new StringTextComponent(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format(TRANSLATE_DESCRIPTION)));
				event.getToolTip().add(new StringTextComponent(TextFormatting.RED + I18n.format(TRANSLATE_INFO, itemsStage)));
			}

			if (enchantStage != null && !GameStageHelper.hasStage(player,enchantStage) && ConfigurationHandler.changeRestrictionTooltip) {

				event.getToolTip().add(new StringTextComponent(" "));
				event.getToolTip().add(new StringTextComponent(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format(TRANSLATE_ENCHANT_DESCRIPTION)));
				event.getToolTip().add(new StringTextComponent(TextFormatting.RED + I18n.format(TRANSLATE_INFO, enchantStage)));
			}

			// Adds info about which stage the item is added to. This is more of a debug thing.
			else if (itemsStage != null && (event.getPlayer() != null && event.getPlayer().isCreative() || event.getFlags() == ITooltipFlag.TooltipFlags.ADVANCED)) {

				event.getToolTip().add(new StringTextComponent(TextFormatting.BLUE + I18n.format(TRANSLATE_STAGE) + " " + TextFormatting.WHITE + itemsStage));
			}

			// Removes tooltip info that has been restricted.
			for (final String tipStage : StageUtils.tooltipStages.keySet()) {

				if (!GameStageHelper.hasStage(player, tipStage)) {

					for (final Iterator<ITextComponent> iterator = event.getToolTip().iterator(); iterator.hasNext();) {

						final String tooltipLine = iterator.next().getString();

						for (final String restricted : StageUtils.tooltipStages.get(tipStage)) {

							if (tooltipLine.startsWith(restricted)) {

								iterator.remove();
							}
						}
					}
				}
			}
		}
	}

//	@OnlyIn(Dist.CLIENT)
//	@SubscribeEvent
//	public void onClientSync (StagesSyncedEvent event) {
//
//		if (Loader.isModLoaded("jei")) {
//
//			PluginItemStages.syncHiddenItems(event.getPlayer());
//		}
//	}

//	@SubscribeEvent
//	@OnlyIn(Dist.CLIENT)
//	public void onClientLoadComplete (FMLLoadCompleteEvent event) {
//
//		// Add a resource reload listener to keep up to sync with JEI.
//		((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(listener -> {
//
//			if (Loader.isModLoaded("jei")) {
//
//				LOG.info("Resyncing JEI info.");
//				PluginItemStages.syncHiddenItems(PlayerUtils.getClientPlayerSP());
//			}
//		});
//	}
}
