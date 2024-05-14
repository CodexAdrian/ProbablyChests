package org.cloudwarp.probablychests.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.cloudwarp.probablychests.ProbablyChests;

public record MimicInventoryPacket(
    int size,
    int entityId,
    byte syncId
) implements CustomPayload {

    public static final Id<MimicInventoryPacket> ID = new Id<>(ProbablyChests.id("mimic_inventory_packet"));
    public static final PacketCodec<ByteBuf, MimicInventoryPacket> CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER,
        MimicInventoryPacket::size,
        PacketCodecs.VAR_INT,
        MimicInventoryPacket::entityId,
        PacketCodecs.BYTE,
        MimicInventoryPacket::syncId,
        MimicInventoryPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
