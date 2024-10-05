package org.cloudwarp.probablychests.neoforge;

import org.cloudwarp.ProbablyChests;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ProbablyChests.MOD_ID)
public class ProbablyChestsNeoForge {

    public ProbablyChestsNeoForge(IEventBus bus) {
        ProbablyChests.init();
    }
}
