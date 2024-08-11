package org.cloudwarp.probablychests.network;

import com.teamresourceful.resourcefullib.common.network.Network;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCCommonNetworking {
    public static final Network NETWORK = new Network(ProbablyChests.id("main"), 0);

    public static void init() {
        NETWORK.register(ConfigPacket.TYPE);
        NETWORK.register(MimicInventoryPacket.TYPE);
    }
}
