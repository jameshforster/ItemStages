package com.tol.researchstages.compat.patchouli.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.function.UnaryOperator;

abstract class ResearchItemComponent implements ICustomComponent {
    transient public int componentX;
    transient public int componentY;

    transient public String stageName;

    abstract public List<ItemStack> getItems(String stage);

    @Override
    public void build(int x, int y, int pageNum) {
        this.componentX = x;
        this.componentY = y;
    }

    @Override
    public void render(MatrixStack matrixStack, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        int x = componentX;
        int y = componentY;

        for (ItemStack item : getItems(stageName)) {
            context.renderItemStack(matrixStack, x, y, mouseX, mouseY, item);
            if (x == componentX + 80) {
                x = componentX;
                y = y + 20;
            } else {
                x = x + 20;
            }
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        stageName = lookup.apply(IVariable.wrap("#stageName#")).asString();
    }
}
