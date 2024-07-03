package org.cloudwarp.probablychests.utils;

import net.minecraft.util.StringIdentifiable;

public enum PCChestState implements StringIdentifiable {
	OPENED("opened"),
	CLOSED("closed");

	private final String name;


	private PCChestState (String name) {
		this.name = name;
	}

	@Override
	public String asString () {
		return this.name;
	}
}
