package org.cloudwarp.probablychests.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.cloudwarp.probablychests.ProbablyChests;

public record MimicInventoryPacket(
    int size,
    int entityId,
    byte syncId
) implements CustomPacketPayload {

    public static final Type<MimicInventoryPacket> ID = new Type<>(ProbablyChests.id("mimic_inventory_packet"));
    public static final StreamCodec<ByteBuf, MimicInventoryPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        MimicInventoryPacket::size,
        ByteBufCodecs.VAR_INT,
        MimicInventoryPacket::entityId,
        ByteBufCodecs.BYTE,
        MimicInventoryPacket::syncId,
        MimicInventoryPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
