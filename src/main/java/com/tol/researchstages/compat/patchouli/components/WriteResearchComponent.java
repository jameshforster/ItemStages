package com.tol.researchstages.compat.patchouli.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tol.researchstages.items.ResearchItem;
import com.tol.researchstages.networking.NetworkingHandler;
import com.tol.researchstages.utils.ItemStackUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import org.apache.logging.log4j.LogManager;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;


abstract class WriteResearchComponent implements ICustomComponent {
    transient public String stageName;

    transient public int componentX;
    transient public int componentY;

    @Override
    public void build(int x, int y, int pageNum) {
        this.componentX = x;
        this.componentY = y;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        stageName = lookup.apply(IVariable.wrap("#stageName#")).asString();
    }

    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        int trueComponentX = componentX + 105;
        int trueComponentY = componentY + 45;
        LogManager.getLogger().error("\n#####\n" +
                "CLICK REGISTERED AT: " + mouseX + "," + mouseY +
                "\nCOMPONENT X: " + trueComponentX +
                "\nCOMPONENT Y: " + trueComponentY +
                "\n#####\n");
        if (mouseX >= trueComponentX && mouseX <= trueComponentX + 15 && mouseY >= trueComponentY && mouseY <= trueComponentY + 15) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (GameStageHelper.hasStage(player, stageName)) {
                for (ItemStack itemStack : player.inventory.mainInventory) {
                    if (itemStack.isItemEqual(getDefaultItemStack()) && itemStack.getItem() instanceof ResearchItem) {
                        CompoundNBT tag = itemStack.getOrCreateTag();
                        if (tag.getString("stageName").equals("")) {
                            NetworkingHandler.sendResearchWritingMessageToServer(stageName, itemStack);
                            tag.putString("stageName", stageName);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        context.renderItemStack(matrixStack, componentX, componentY, mouseX, mouseY, getDefaultItemStack());
    }

    abstract ItemStack getDefaultItemStack();
}
