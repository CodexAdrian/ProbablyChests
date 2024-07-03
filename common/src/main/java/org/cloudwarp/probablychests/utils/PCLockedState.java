package org.cloudwarp.probablychests.utils;

import net.minecraft.util.StringRepresentable;

public enum PCLockedState  implements StringRepresentable {
	LOCKED("locked"),
	UNLOCKED("unlocked");
	private final String name;

	private PCLockedState (String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName () {
		return this.name;
	}
}
