package com.tol.researchstages.research;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class ChainedCondition implements ICondition {
    public List<ICondition> conditions;

    public ChainedCondition(List<ICondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean checkCondition(ClientPlayerEntity player) {
        boolean allPass = true;
        for (ICondition condition : conditions) {
            if (!condition.checkCondition(player)) {
                allPass = false;
            }
        }
        return allPass;
    }

    public void updateDependencyCondition(DependencyCondition newCondition) {
        DependencyCondition oldCondition = null;
        int oldConditionIndex = -1;
        for (ICondition condition : conditions) {
            if (condition instanceof DependencyCondition) {
                oldConditionIndex = conditions.indexOf(condition);
                oldCondition = (DependencyCondition) condition;
            }
        }
        if (oldCondition != null) {
            for (String dependencyName : newCondition.dependencyNames) {
                if (!oldCondition.dependencyNames.contains(dependencyName)) {
                    oldCondition.dependencyNames.add(dependencyName);
                }
            }
            conditions.add(oldConditionIndex, oldCondition);
        } else {
            conditions.add(newCondition);
        }
    }

    public void updateCompletedCondition(CompletedCondition newCondition) {
        CompletedCondition oldCondition = null;
        for (ICondition condition : conditions) {
            if (condition instanceof CompletedCondition) {
                oldCondition = (CompletedCondition) condition;
            }
        }
        if (oldCondition == null) {
            conditions.add(newCondition);
        }
    }

    public void updateItemCondition(ItemCondition newCondition) {
        ItemCondition oldCondition = null;
        for (ICondition condition : conditions) {
            if (condition instanceof ItemCondition) {
                oldCondition = (ItemCondition) condition;
            }
        }
        if (oldCondition == null) {
            conditions.add(newCondition);
        }
    }
}
