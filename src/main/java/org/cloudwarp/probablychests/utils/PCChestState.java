package org.cloudwarp.probablychests.utils;

import net.minecraft.util.StringRepresentable;

public enum PCChestState implements StringRepresentable {
	OPENED("opened"),
	CLOSED("closed");

	private final String name;


	private PCChestState (String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName () {
		return this.name;
	}
}
