package org.cloudwarp.probablychests.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCSounds {
	public static final ResourceLocation BELL_HIT_1_ID = ProbablyChests.id("bell_hit1");
	public static final ResourceLocation BELL_HIT_2_ID = ProbablyChests.id("bell_hit2");
	public static final ResourceLocation BELL_HIT_4_ID = ProbablyChests.id("bell_hit4");
	public static final ResourceLocation CLOSE_2_ID = ProbablyChests.id("close2");
	public static final ResourceLocation MIMIC_BITE_ID = ProbablyChests.id("mimic_bite");
	public static final ResourceLocation APPLY_LOCK_1_ID = ProbablyChests.id("apply_lock1");
	public static final ResourceLocation APPLY_LOCK_2_ID = ProbablyChests.id("apply_lock2");
	public static final ResourceLocation LOCK_UNLOCK_ID = ProbablyChests.id("lock_unlock");
	public static SoundEvent BELL_HIT_1 = SoundEvent.createVariableRangeEvent(BELL_HIT_1_ID);
	public static SoundEvent BELL_HIT_2 = SoundEvent.createVariableRangeEvent(BELL_HIT_2_ID);
	public static SoundEvent BELL_HIT_4 = SoundEvent.createVariableRangeEvent(BELL_HIT_4_ID);
	public static SoundEvent CLOSE_2  = SoundEvent.createVariableRangeEvent(CLOSE_2_ID);
	public static SoundEvent MIMIC_BITE = SoundEvent.createVariableRangeEvent(MIMIC_BITE_ID);
	public static SoundEvent APPLY_LOCK1 = SoundEvent.createVariableRangeEvent(APPLY_LOCK_1_ID);
	public static SoundEvent APPLY_LOCK2 = SoundEvent.createVariableRangeEvent(APPLY_LOCK_2_ID);
	public static SoundEvent LOCK_UNLOCK = SoundEvent.createVariableRangeEvent(LOCK_UNLOCK_ID);

	public static void init () {
		Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_1_ID, BELL_HIT_1);
		Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_2_ID, BELL_HIT_2);
		Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_4_ID, BELL_HIT_4);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CLOSE_2_ID, CLOSE_2);
		Registry.register(BuiltInRegistries.SOUND_EVENT, MIMIC_BITE_ID, MIMIC_BITE);
		Registry.register(BuiltInRegistries.SOUND_EVENT, APPLY_LOCK_1_ID, APPLY_LOCK1);
		Registry.register(BuiltInRegistries.SOUND_EVENT, APPLY_LOCK_2_ID, APPLY_LOCK2);
		Registry.register(BuiltInRegistries.SOUND_EVENT, LOCK_UNLOCK_ID, LOCK_UNLOCK);
	}
}
