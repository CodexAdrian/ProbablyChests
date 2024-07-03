package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PCPotFeatureConfig(FloatProvider mimicChance) implements FeatureConfiguration {
	public static final Codec<PCPotFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			FloatProvider.CODEC.fieldOf("rarity").forGetter(PCPotFeatureConfig::mimicChance)
	).apply(instance, instance.stable(PCPotFeatureConfig::new)));
}
