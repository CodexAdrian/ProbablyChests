package org.cloudwarp.probablychests.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PCCommonNetworking {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(ConfigPacket.ID, ConfigPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(MimicInventoryPacket.ID, MimicInventoryPacket.CODEC);


    }
}
