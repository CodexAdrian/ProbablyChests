package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.registry.PCTags;
import org.cloudwarp.probablychests.utils.PCLockedState;

public class SurfaceChestFeature extends Feature<NoneFeatureConfiguration> {
	public SurfaceChestFeature (Codec<NoneFeatureConfiguration> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place (FeaturePlaceContext<NoneFeatureConfiguration> context) {
		net.minecraft.util.RandomSource random = context.random();
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos pos = context.origin();
		NoneFeatureConfiguration config = context.config();
		BlockState blockToBePlaced = null;
		// Set block type based on environment
		boolean hasVoidLock = false;
		PCLockedState lockedState = PCLockedState.UNLOCKED;
		if (! structureWorldAccess.getBlockState(pos.below()).isRedstoneConductor(structureWorldAccess, pos)) {
			return false;
		}
		boolean isEnd = structureWorldAccess.getBiome(pos).is(BiomeTags.IS_END);
		Biome biome = structureWorldAccess.getBiome(pos).value();

		if (isEnd) {
			hasVoidLock = true;
			blockToBePlaced = PCBlocks.SHADOW_CHEST.get().defaultBlockState();
		} else if (structureWorldAccess.getBiome(pos).is(BiomeTags.IS_OCEAN)) {
			if(structureWorldAccess.getBiome(pos).is(PCTags.icy())){
				blockToBePlaced = PCBlocks.ICE_CHEST.get().defaultBlockState();
			}else{
				return false;
			}
		} else {
			if (structureWorldAccess.getBiome(pos).is(PCTags.floral()) ||
					structureWorldAccess.getBiome(pos).is(PCTags.flowerForest()) ||
					structureWorldAccess.getBiome(pos).is(PCTags.climateTemperate())) {
				blockToBePlaced = PCBlocks.LUSH_CHEST.get().defaultBlockState();
			} else if (isBiomeWithinTempRange(biome, 1F, 10.0F) ||
					structureWorldAccess.getBiome(pos).is(PCTags.climateHot())) {
				blockToBePlaced = PCBlocks.ROCKY_CHEST.get().defaultBlockState();
			} else if(structureWorldAccess.getBiome(pos).is(PCTags.snowy())){
				blockToBePlaced = PCBlocks.ICE_CHEST.get().defaultBlockState();
			} else if(structureWorldAccess.getBiome(pos).is(PCTags.beach())){
				blockToBePlaced = PCBlocks.CORAL_CHEST.get().defaultBlockState();
			}else{
				blockToBePlaced = PCBlocks.NORMAL_CHEST.get().defaultBlockState();
			}
		}

		if(hasVoidLock){
			lockedState = PCLockedState.LOCKED;
		}
		structureWorldAccess.setBlock(pos, blockToBePlaced.setValue(PCProperties.PC_LOCKED_STATE, lockedState), 3);
		/*
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BlockPos debugPos = pos;
			for (int i = 0; i < 60; i++) {
				structureWorldAccess.setBlock(debugPos = debugPos.above(), Blocks.END_ROD.defaultBlockState(), 3);
			}
		}
		 */
		PCBaseChestBlockEntity chest = (PCBaseChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
			chest.hasVoidLock = hasVoidLock;
			chest.isLocked = hasVoidLock;
		}
		return true;
	}

	public static boolean isBiomeWithinTempRange (Biome biome, float minTemp, float maxTemp) {
		return biome.getBaseTemperature() >= minTemp && biome.getBaseTemperature() < maxTemp;
	}
}
