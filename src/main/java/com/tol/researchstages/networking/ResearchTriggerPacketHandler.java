package com.tol.researchstages.networking;

import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class ResearchTriggerPacketHandler {

    String stageName;
    ItemStack item;
    int tableLevel;

    public ResearchTriggerPacketHandler(String stageName, ItemStack item, int tableLevel) {
        this.stageName = stageName;
        this.item = item;
        this.tableLevel = tableLevel;
    }

    public ResearchTriggerPacketHandler(CompoundNBT nbt) {
        this.stageName = nbt.getString("stageName");
        this.item = ItemStack.read((CompoundNBT) nbt.get("researchItem"));
        this.tableLevel = nbt.getInt("tableLevel");
    }

    public void writeToBuffer(PacketBuffer pkt) {
        pkt.writeCompoundTag(this.toNbt());
    }

    private CompoundNBT toNbt() {
        CompoundNBT tag = new CompoundNBT();

        tag.put("stageName", StringNBT.valueOf(stageName));
        tag.put("researchItem", item.write(new CompoundNBT()));
        tag.put("tableLevel", IntNBT.valueOf(tableLevel));

        return tag;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LogManager.getLogger().info("[RESEARCHSTAGES] Logged message with research trigger.");
            NetworkDirection direction = ctx.get().getDirection();
            if (direction != NetworkDirection.PLAY_TO_CLIENT) {
                LogManager.getLogger().info("[RESEARCHSTAGES] Logged message to server from client.");
                ServerPlayerEntity sender = ctx.get().getSender();
                if (ResearchStageUtils.RESEARCH_STAGES.containsKey(stageName)) {
                    ResearchStage stage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
                    ResearchStageUtils.doResearch(sender, stage, item, tableLevel);
                }
            }
        });
        return true;
    }
}
