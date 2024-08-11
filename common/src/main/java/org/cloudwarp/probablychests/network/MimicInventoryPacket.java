package org.cloudwarp.probablychests.network;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.NetworkHandle;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

public record MimicInventoryPacket(
    int size,
    int entityId,
    byte syncId
) implements Packet<MimicInventoryPacket> {
    public static final ClientboundPacketType<MimicInventoryPacket> TYPE = CodecPacketType.Client.create(
        ProbablyChests.id("mimic_inventory"),
        ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(MimicInventoryPacket::size),
            ByteCodec.INT.fieldOf(MimicInventoryPacket::entityId),
            ByteCodec.BYTE.fieldOf(MimicInventoryPacket::syncId),
            MimicInventoryPacket::new
        ),
        NetworkHandle.handle((payload) -> {
            int id = payload.entityId();
            Player player = Minecraft.getInstance().player;
            Entity entity = player.level().getEntity(id);
            if (entity instanceof PCTameablePetWithInventory mimicEntity && player.containerMenu instanceof PCMimicScreenHandler mimicScreenHandler) {
                mimicScreenHandler.setMimicEntity(mimicEntity);
            }
        })
    );

    @Override
    public PacketType<MimicInventoryPacket> type() {
        return TYPE;
    }
}
