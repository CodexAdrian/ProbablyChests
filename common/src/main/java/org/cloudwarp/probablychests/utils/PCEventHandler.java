package org.cloudwarp.probablychests.utils;

import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.network.ConfigPacket;

public class PCEventHandler {

	private PCEventHandler (){}

	public static void registerEvents() {
		/* TODO: Implement xplat
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ConfigPacket packet = new ConfigPacket(ProbablyChests.configToNBT());
			ServerPlayNetworking.send(handler.player, packet);
		});
		 */
	}
}
