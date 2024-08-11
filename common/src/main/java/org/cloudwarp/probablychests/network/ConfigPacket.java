package org.cloudwarp.probablychests.network;

import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.NetworkHandle;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import net.minecraft.nbt.CompoundTag;
import org.cloudwarp.probablychests.ProbablyChests;

public record ConfigPacket(CompoundTag config) implements Packet<ConfigPacket> {

    public static final ClientboundPacketType<ConfigPacket> TYPE = CodecPacketType.Client.create(
        ProbablyChests.id("probably_chests_config_update"),
        ExtraByteCodecs.NONNULL_COMPOUND_TAG.map(ConfigPacket::new, ConfigPacket::config),
        NetworkHandle.handle((packet) -> ProbablyChests.loadedConfig = ProbablyChests.nbtToConfig(packet.config()))
    );

    @Override
    public PacketType<ConfigPacket> type() {
        return TYPE;
    }
}
