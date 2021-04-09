package com.tol.researchstages.networking;

import com.tol.researchstages.research.ResearchStage;
import com.tol.researchstages.utils.ResearchStageUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResearchStudyPacketHandler {

    String stageName;
    String itemType;

    public ResearchStudyPacketHandler(String stageName, String itemType) {
        this.stageName = stageName;
        this.itemType = itemType;
    }

    public ResearchStudyPacketHandler(CompoundNBT nbt) {
        this.stageName = nbt.getString("stageName");
        this.itemType = nbt.getString("itemType");
    }

    public void writeToBuffer(PacketBuffer pkt) {
        pkt.writeCompoundTag(this.toNbt());
    }

    private CompoundNBT toNbt() {
        CompoundNBT tag = new CompoundNBT();

        tag.put("stageName", StringNBT.valueOf(stageName));
        tag.put("itemType", StringNBT.valueOf(itemType));

        return tag;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NetworkDirection direction = ctx.get().getDirection();
            if (direction != NetworkDirection.PLAY_TO_CLIENT) {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (ResearchStageUtils.RESEARCH_STAGES.containsKey(stageName)) {
                    ResearchStage stage = ResearchStageUtils.RESEARCH_STAGES.get(stageName);
                    ResearchStageUtils.studyResearch(sender, stage, itemType);
                }
            }
        });
        return true;
    }
}
