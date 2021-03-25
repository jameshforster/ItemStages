package com.tol.researchstages.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ResearchEvents {

    public static class ResearchSyncedEvent extends PlayerEvent {

        public ResearchSyncedEvent(PlayerEntity player) {
            super(player);
        }
    }
}
