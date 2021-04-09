package com.tol.researchstages.networking;

import com.tol.researchstages.items.ResearchItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResearchWritingPacketHandler {

    ItemStack clientItemStack;
    String stageName;

    public ResearchWritingPacketHandler(ItemStack itemStack, String stageName) {
        this.clientItemStack = itemStack;
        this.stageName = stageName;
    }

    public ResearchWritingPacketHandler(CompoundNBT nbt) {
        this.clientItemStack = ItemStack.read(nbt.getCompound("researchItem"));
        this.stageName = nbt.getString("stageName");
    }

    public void writeToBuffer(PacketBuffer pkt) {
        pkt.writeCompoundTag(this.toNbt());
    }

    private CompoundNBT toNbt() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("stageName", stageName);
        tag.put("researchItem", clientItemStack.write(new CompoundNBT()));

        return tag;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NetworkDirection direction = ctx.get().getDirection();
            if (direction != NetworkDirection.PLAY_TO_CLIENT) {
                ServerPlayerEntity sender = ctx.get().getSender();
                for (ItemStack itemStack : sender.inventory.mainInventory) {
                    if (itemStack.isItemEqual(clientItemStack) && itemStack.getItem() instanceof ResearchItem) {
                        CompoundNBT tag = itemStack.getOrCreateTag();
                        if (tag.getString("stageName").equals("")) {
                            tag.putString("stageName", stageName);
                        }
                    }
                }
            }
        });
        return true;
    }
}
