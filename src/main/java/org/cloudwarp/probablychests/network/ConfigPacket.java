package org.cloudwarp.probablychests.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.cloudwarp.probablychests.ProbablyChests;

public record ConfigPacket(NbtCompound config) implements CustomPayload {

    public static final Id<ConfigPacket> ID = new Id<>(ProbablyChests.id("probably_chests_config_update"));
    public static final PacketCodec<ByteBuf, ConfigPacket> CODEC = PacketCodecs.NBT_COMPOUND.xmap(ConfigPacket::new, ConfigPacket::config);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
