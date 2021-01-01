package com.tol.itemstages.events;

import com.tol.itemstages.capabilities.ResearchCapability;
import com.tol.itemstages.networking.NetworkingHandler;
import com.tol.itemstages.research.PlayerResearch;
import com.tol.itemstages.utils.ItemStageUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents {
    boolean messageSent = false;

    @SubscribeEvent
    public void onPlayerDig(PlayerEvent.BreakSpeed event) {

    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {

    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {

        if (event.getEntity() instanceof PlayerEntity && !event.getEntityLiving().world.isRemote) {
            final PlayerEntity player = (PlayerEntity) event.getEntity();

            final ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);

            if(ItemStageUtils.INSTANCE.hasStagedItem(heldItem)) {
				if (!ItemStageUtils.INSTANCE.hasAllStages(player, heldItem)) {
					heldItem.setDisplayName(new StringTextComponent(ItemStageUtils.INSTANCE.getHiddenName(heldItem)));
				} else {
					heldItem.removeChildTag("display");
				}
			}
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            PlayerResearch research = event.getPlayer().getCapability(ResearchCapability.PLAYER_RESEARCH).orElse(new PlayerResearch());
            NetworkingHandler.sendResearchMessageToPlayer(research, (ServerPlayerEntity) event.getPlayer());
        }
    }
}
