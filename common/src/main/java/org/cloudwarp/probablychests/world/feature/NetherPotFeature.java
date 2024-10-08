package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.cloudwarp.probablychests.registry.PCBlocks;;

public class NetherPotFeature extends Feature<PCPotFeatureConfig> {
	public NetherPotFeature (Codec<PCPotFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place (FeaturePlaceContext<PCPotFeatureConfig> context) {
		RandomSource random = context.random();
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos pos = context.origin();
		PCPotFeatureConfig config = context.config();
		structureWorldAccess.setBlock(pos, PCBlocks.NETHER_POT.defaultBlockState(), 3);
		return true;
	}
}
