package com.tol.researchstages.research;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface ICondition {

    public boolean checkCondition(ClientPlayerEntity player);
}
