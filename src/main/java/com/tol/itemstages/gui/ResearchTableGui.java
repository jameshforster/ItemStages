package com.tol.itemstages.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tol.itemstages.containers.ResearchTableContainer;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.StonecutterScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ResearchTableGui extends ContainerScreen<ResearchTableContainer> {

	private StonecutterScreen example = null;
	private EnchantmentScreen example2 = null;

	private ResourceLocation GUI = new ResourceLocation("researchstages", "textures/gui/research_table_gui.png");

	public ResearchTableGui(ResearchTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		for(int k = 0; k < 3; ++k) {
			double d0 = mouseX - (double)(i + 60);
			double d1 = mouseY - (double)(j + 14 + 19 * k);
			if (d0 >= 0.0D && d1 >= 0.0D && d0 < 108.0D && d1 < 19.0D && this.container.canResearch(this.minecraft.player, k)) {
				this.container.doResearch(this.minecraft.player, k);
				return true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
