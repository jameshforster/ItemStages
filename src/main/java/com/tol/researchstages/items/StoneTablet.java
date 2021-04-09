package com.tol.researchstages.items;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTablet extends Item implements ResearchItem {
    public StoneTablet(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        CompoundNBT nbtTagCompound = itemStackIn.getTag();
        String stageName = null;
        if (nbtTagCompound != null) {
            stageName = nbtTagCompound.getString("stageName");
        }
        if (playerIn instanceof ClientPlayerEntity && stageName != null) {
            doResearch((ClientPlayerEntity) playerIn, stageName, "stone_tablet");
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag() && !stack.getTag().getString("stageName").equals("")) {
            tooltip.add(new StringTextComponent(stack.getTag().getString("stageName")));
        } else {
            tooltip.add(new StringTextComponent("Unwritten"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
