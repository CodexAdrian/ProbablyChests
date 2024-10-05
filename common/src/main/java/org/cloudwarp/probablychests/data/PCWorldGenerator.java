package org.cloudwarp.probablychests.data;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import org.cloudwarp.probablychests.ProbablyChests;

import java.util.concurrent.CompletableFuture;

public class PCWorldGenerator extends FabricDynamicRegistryProvider {
	public PCWorldGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
	}

	@Override
	public String getName() {
		return ProbablyChests.MOD_ID;
	}
}
