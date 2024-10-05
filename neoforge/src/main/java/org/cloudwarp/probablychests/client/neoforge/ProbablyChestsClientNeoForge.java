package org.cloudwarp.probablychests.client.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProbablyChestsClientNeoForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ProbablyChestsClient::init);
    }
}
