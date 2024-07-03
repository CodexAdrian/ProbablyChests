package org.cloudwarp.probablychests.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.cloudwarp.probablychests.ProbablyChests;

public record ConfigPacket(CompoundTag config) implements CustomPacketPayload {

    public static final Type<ConfigPacket> ID = new Type<>(ProbablyChests.id("probably_chests_config_update"));
    public static final StreamCodec<ByteBuf, ConfigPacket> CODEC = ByteBufCodecs.COMPOUND_TAG.map(ConfigPacket::new, ConfigPacket::config);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
