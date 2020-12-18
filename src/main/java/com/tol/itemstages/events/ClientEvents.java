package com.tol.itemstages.events;

import com.tol.itemstages.compat.jei.PluginItemStages;
import com.tol.itemstages.stages.StageUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getPlayer() != null && StageUtils.INSTANCE.hasStagedItem(event.getItemStack())) {
            if (!StageUtils.INSTANCE.hasAllStages(event.getPlayer(), event.getItemStack())) {
                event.getToolTip().clear();
                event.getToolTip().add(new StringTextComponent(StageUtils.INSTANCE.getHiddenName(event.getItemStack())));
            } else {
                event.getToolTip().clear();
                event.getToolTip().addAll(StageUtils.INSTANCE.getStagedItem(event.getItemStack()).getTooltip(event.getPlayer(), ITooltipFlag.TooltipFlags.NORMAL));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientSync(StagesSyncedEvent event) {
        jeiConditional(event.getPlayer());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientLoadComplete(FMLLoadCompleteEvent event) {
        jeiConditional(PlayerUtils.getClientPlayer());
    }

    private void jeiConditional(PlayerEntity player) {
        if (ModList.get().getModContainerById("jei").isPresent()) {
            PluginItemStages.syncHiddenItems(player);
        }
    }
}
