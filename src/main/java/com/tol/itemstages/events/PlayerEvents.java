package com.tol.itemstages.events;

import com.tol.itemstages.ConfigurationHandler;
import com.tol.itemstages.stages.StageUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "itemstages")
public class PlayerEvents {

	@SubscribeEvent
	public void onPlayerDig (PlayerEvent.BreakSpeed event) {

		if (!ConfigurationHandler.allowInteractRestricted && !event.getPlayer().isCreative()) {

			ItemStack heldItem = event.getPlayer().getHeldItemMainhand();

			final String stage = StageUtils.getStage(heldItem);

			final String enchantStage = StageUtils.getEnchantStage(heldItem);

			if ((stage != null && !GameStageHelper.hasStage(event.getPlayer(), stage)) ||
					(enchantStage != null && !GameStageHelper.hasStage(event.getPlayer(), enchantStage))) {

				event.setNewSpeed(-1f);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttack (AttackEntityEvent event) {

		if (!ConfigurationHandler.allowInteractRestricted && !event.getPlayer().isCreative()) {

			final String stage = StageUtils.getEnchantStage(event.getPlayer().getHeldItemMainhand());

			if ((stage != null && !GameStageHelper.hasStage(event.getPlayer(), stage))) {

				if (event.getPlayer().world.getGameTime() % 2 == 0) {

					StageUtils.sendAttackFailMessage(event.getPlayer(), event.getPlayer().getHeldItemMainhand());
				}
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract (PlayerInteractEvent event) {

		if (event.isCancelable() && !ConfigurationHandler.allowInteractRestricted && !event.getPlayer().isCreative()) {

			final String stage = StageUtils.getStage(event.getItemStack());

			if (stage != null && !GameStageHelper.hasStage(event.getPlayer(), stage)) {

				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onLivingUpdate (LivingEvent.LivingUpdateEvent event) {

		if (event.getEntity() instanceof PlayerEntity && !event.getEntityLiving().world.isRemote) {

			final PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			// Exit early if creative mode.
			if (player.isCreative()) {

				return;
			}

			for (final EquipmentSlotType slot : EquipmentSlotType.values()) {

				// Skips the item if the slot type was configured to be ignored.
				if (ConfigurationHandler.allowHoldingRestricted && slot.getSlotType().equals(EquipmentSlotType.Group.HAND) || ConfigurationHandler.allowEquipRestricted && slot.getSlotType() == EquipmentSlotType.Group.ARMOR) {

					continue;
				}

				final ItemStack stack = player.getItemStackFromSlot(slot);
				final String stage = StageUtils.getStage(stack);

				String enchantStage = null;

				if (!ConfigurationHandler.allowHoldingRestrictedEnchant || !(slot.getSlotType() == EquipmentSlotType.Group.HAND)) {

					enchantStage = StageUtils.getEnchantStage(stack);
				}

				if ((stage != null && !GameStageHelper.hasStage(player, stage)) ||
						(enchantStage != null && !GameStageHelper.hasStage(player, enchantStage))) {

					player.setItemStackToSlot(slot, ItemStack.EMPTY);
					player.dropItem(stack, false);
					StageUtils.sendDropMessage(player, stack);
				}
			}
		}
	}
}
