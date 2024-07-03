package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PCChestFeatureConfig(FloatProvider rarity) implements FeatureConfiguration {
	public static final Codec<PCChestFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			FloatProvider.CODEC.fieldOf("chance").forGetter(PCChestFeatureConfig::rarity)
	).apply(instance, instance.stable(PCChestFeatureConfig::new)));
}
