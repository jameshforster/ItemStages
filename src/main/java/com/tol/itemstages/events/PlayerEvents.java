package com.tol.itemstages.events;

import com.tol.itemstages.stages.StageUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
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

            if(!StageUtils.INSTANCE.hasAllStages(player, heldItem)) {
                heldItem.setDisplayName(new StringTextComponent(StageUtils.INSTANCE.getHiddenName(heldItem)));
            }
        }
    }
}
