package com.tol.itemstages.research;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public class DefaultCondition implements ICondition {
    public static DefaultCondition INSTANCE = new DefaultCondition();

    @Override
    public boolean checkCondition(ClientPlayerEntity player) {
        return true;
    }
}
