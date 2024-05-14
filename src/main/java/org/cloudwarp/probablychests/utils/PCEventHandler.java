package org.cloudwarp.probablychests.utils;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.network.ConfigPacket;

public class PCEventHandler {

	private PCEventHandler (){}

	public static void registerEvents(){
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ConfigPacket packet = new ConfigPacket(ProbablyChests.configToNBT());
			ServerPlayNetworking.send(handler.player, packet);
		});
	}
}
