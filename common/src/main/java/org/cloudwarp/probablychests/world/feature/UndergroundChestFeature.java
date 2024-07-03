package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.utils.PCLockedState;

import java.util.Optional;

public class UndergroundChestFeature extends Feature<NoneFeatureConfiguration> {
	public UndergroundChestFeature (Codec<NoneFeatureConfiguration> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place (FeaturePlaceContext<NoneFeatureConfiguration> context) {
		net.minecraft.util.RandomSource random = context.random();
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos pos = context.origin().above();
		NoneFeatureConfiguration config = context.config();
		BlockState blockToBePlaced = null;
		boolean isWater = structureWorldAccess.getBlockState(pos).is(Blocks.WATER);
		boolean isNether = structureWorldAccess.dimensionType().ultraWarm();
		boolean hasGoldLock = false;
		PCLockedState lockedState = PCLockedState.UNLOCKED;
		if (isWater) {
			if (random.nextFloat() < 0.85F) {
				return false;
			}
			if(structureWorldAccess.getBlockState(pos.above()).isRedstoneConductor(structureWorldAccess,pos.above())){
				return false;
			}
			if (structureWorldAccess.getBiome(pos).is(ConventionalBiomeTags.ICY) || structureWorldAccess.getBiome(pos).is(ConventionalBiomeTags.SNOWY)) {
				blockToBePlaced = PCBlocks.ICE_CHEST.defaultBlockState();
			} else {
				blockToBePlaced = PCBlocks.CORAL_CHEST.defaultBlockState();
			}
		} else {
			Optional<Column> optional = Column.scan(structureWorldAccess, pos, 64, UndergroundChestFeature::canGenerate, UndergroundChestFeature::canReplace);
			if (! optional.isPresent() || ! (optional.get() instanceof Column.Range)) {
				return false;
			}
			Column.Range bounded = (Column.Range) optional.get();
			if (bounded.height() < 3) {
				return false;
			}
			if (bounded.floor() > pos.getY()) {
				return false;
			}
			if(isNether){
				blockToBePlaced = PCBlocks.NETHER_CHEST.defaultBlockState();
			}
			BlockPos biomeCheckPos = pos.relative(Direction.UP, 5);
			// Set block type based on environment
			if (random.nextFloat() < 0.85F) {
				if (structureWorldAccess.getBiome(biomeCheckPos).is(Biomes.LUSH_CAVES)) {
					blockToBePlaced = PCBlocks.LUSH_CHEST.defaultBlockState();
				} else if (structureWorldAccess.getBiome(biomeCheckPos).is(Biomes.DRIPSTONE_CAVES)) {
					blockToBePlaced = PCBlocks.ROCKY_CHEST.defaultBlockState();
				}
			}
			if (blockToBePlaced == null) {
				if (pos.getY() <= 0) {
					if (random.nextFloat() < 0.25F) {
						blockToBePlaced = PCBlocks.GOLD_CHEST.defaultBlockState();
						hasGoldLock = true;
					} else {
						blockToBePlaced = PCBlocks.STONE_CHEST.defaultBlockState();
					}
				} else {
					if (structureWorldAccess.getBiome(biomeCheckPos).is(ConventionalBiomeTags.SNOWY) || structureWorldAccess.getBiome(biomeCheckPos).is(ConventionalBiomeTags.ICY)) {
						blockToBePlaced = PCBlocks.ICE_CHEST.defaultBlockState();
					} else {
						if (random.nextFloat() < 0.25F) {
							blockToBePlaced = PCBlocks.LUSH_CHEST.defaultBlockState();
						} else {
							blockToBePlaced = PCBlocks.NORMAL_CHEST.defaultBlockState();
						}
					}
				}
			}
		}
		if(hasGoldLock){
			lockedState = PCLockedState.LOCKED;
		}
		//structureWorldAccess.setBlockState(pos, blockToBePlaced.with(PCProperties.PC_LOCKED_STATE, lockedState), 3);
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BlockPos debugPos = pos;
			for (int i = 0; i < 40; i++) {
				structureWorldAccess.setBlock(debugPos = debugPos.above(), Blocks.END_ROD.defaultBlockState(), 3);
			}
		}
		structureWorldAccess.setBlock(pos, blockToBePlaced.setValue(BlockStateProperties.WATERLOGGED, isWater).setValue(PCProperties.PC_LOCKED_STATE,lockedState), 3);
		PCBaseChestBlockEntity chest = (PCBaseChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
			chest.hasGoldLock = hasGoldLock;
			chest.isLocked = hasGoldLock;
		}
		return true;
	}

	public static boolean canReplace (BlockState state) {
		return state.is(BlockTags.BASE_STONE_OVERWORLD) ||
				state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES) ||
				state.is(BlockTags.REPLACEABLE) ||
				state.is(BlockTags.LUSH_GROUND_REPLACEABLE) ||
				state.is(BlockTags.BASE_STONE_NETHER);
	}

	public static boolean canGenerate (BlockState state) {
		return state.isAir() || state.is(Blocks.WATER);
	}
}
