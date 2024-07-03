package org.cloudwarp.probablychests.registry;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.cloudwarp.probablychests.utils.PCLockedState;

public class PCProperties {
	public static final EnumProperty<PCChestState> PC_CHEST_STATE = EnumProperty.create("pc_chest_state", PCChestState.class);
	public static final EnumProperty<PCLockedState> PC_LOCKED_STATE = EnumProperty.create("pc_locked_state", PCLockedState.class);
}
