package com.tol.researchstages.events;

import com.tol.researchstages.compat.jei.PluginItemStages;
import com.tol.researchstages.compat.patchouli.utils.BookReloadUtils;
import com.tol.researchstages.utils.BlockStageUtils;
import com.tol.researchstages.utils.ItemStageUtils;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import mezz.jei.events.PlayerJoinedWorldEvent;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;

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

    @SubscribeEvent
    public void onClientSync(StagesSyncedEvent event) {
        BookReloadUtils.patchouliUpdateConditional((ClientPlayerEntity) event.getPlayer());
        jeiConditional(event.getPlayer());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientLoadComplete(FMLLoadCompleteEvent event) {
        BookReloadUtils.patchouliUpdateConditional(PlayerUtils.getClientPlayer());
        jeiConditional(PlayerUtils.getClientPlayer());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onJeiFirstLoad(PlayerJoinedWorldEvent event) {
        jeiConditional(PlayerUtils.getClientPlayer());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onWailaTooltipEvent(WailaTooltipEvent event) {
        ResourceLocation blockName = event.getAccessor().getBlockId();
        if (BlockStageUtils.INSTANCE.STAGED_BLOCKS.containsKey(blockName)) {
            if (!BlockStageUtils.INSTANCE.hasAllStages(event.getAccessor().getPlayer(), blockName)) {
                event.getCurrentTip().clear();
                event.getCurrentTip().add(new StringTextComponent(
                        BlockStageUtils.INSTANCE.STAGED_BLOCK_NAMES.getOrDefault(blockName, "Unknown")
                ));
            }
        }
    }

    private void jeiConditional(PlayerEntity player) {
        if (ModList.get().getModContainerById("jei").isPresent()) {
            PluginItemStages.syncHiddenItems(player);
            PluginItemStages.syncHiddenRecipes(player);
        }
    }

}
