package com.tol.itemstages.research;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface ICondition {

    public boolean checkCondition(ClientPlayerEntity player);
}
