package com.tol.itemstages.events;

import com.tol.itemstages.compat.jei.PluginItemStages;
import com.tol.itemstages.stages.ItemStageUtils;
import com.tol.itemstages.stages.RecipeStageUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getPlayer() != null && ItemStageUtils.INSTANCE.hasStagedItem(event.getItemStack())) {
            if (!ItemStageUtils.INSTANCE.hasAllStages(event.getPlayer(), event.getItemStack())) {
                event.getToolTip().clear();
                event.getToolTip().add(new StringTextComponent(ItemStageUtils.INSTANCE.getHiddenName(event.getItemStack())));
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
		RecipeStageUtils.INSTANCE.removeRecipesByOutput();
    }

    private void jeiConditional(PlayerEntity player) {
        if (ModList.get().getModContainerById("jei").isPresent()) {
            PluginItemStages.syncHiddenItems(player);
        }
    }
}
