package org.cloudwarp.probablychests.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCLootTables;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;

public enum PCChestTypes {

	LUSH(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "lush_chest"), "lush_chest"),
	NORMAL(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "normal_chest"), "normal_chest"),
	ROCKY(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "rocky_chest"), "rocky_chest"),
	STONE(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "stone_chest"), "stone_chest"),
	GOLD(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "gold_chest"), "gold_chest"),
	NETHER(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "nether_chest"), "nether_chest"),
	SHADOW(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "shadow_chest"), "shadow_chest"),
	ICE(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "ice_chest"), "ice_chest"),
	CORAL(54, 9, new ResourceLocation(ProbablyChests.MOD_ID, "coral_chest"), "coral_chest");


	public final int size;
	public final int rowLength;
	public final ResourceLocation texture;
	public final String name;

	public static final Codec<PCChestTypes> CODEC = Codec.STRING.xmap(PCChestTypes::valueOf, PCChestTypes::name);

	PCChestTypes (int size, int rowLength, ResourceLocation texture, String name) {
		this.size = size;
		this.rowLength = rowLength;
		this.texture = texture;
		this.name = name;
	}

	public EntityType<PCChestMimic> getMimicType () {
		return switch (this) {
			case LUSH   -> PCEntities.LUSH_CHEST_MIMIC;
			case ROCKY  -> PCEntities.ROCKY_CHEST_MIMIC;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC;
			case STONE  -> PCEntities.STONE_CHEST_MIMIC;
			case GOLD   -> PCEntities.GOLD_CHEST_MIMIC;
			case NETHER -> PCEntities.NETHER_CHEST_MIMIC;
			case SHADOW -> PCEntities.SHADOW_CHEST_MIMIC;
			case ICE    -> PCEntities.ICE_CHEST_MIMIC;
			case CORAL  -> PCEntities.CORAL_CHEST_MIMIC;
			default     -> PCEntities.NORMAL_CHEST_MIMIC;
		};
	}

	public ResourceLocation getLootTable () {
		return switch (this) {
			case LUSH   -> PCLootTables.LUSH_CHEST;
			case ROCKY  -> PCLootTables.ROCKY_CHEST;
			case NORMAL -> PCLootTables.NORMAL_CHEST;
			case STONE  -> PCLootTables.STONE_CHEST;
			case GOLD   -> PCLootTables.GOLD_CHEST;
			case NETHER -> PCLootTables.NETHER_CHEST;
			case SHADOW -> PCLootTables.SHADOW_CHEST;
			case ICE    -> PCLootTables.ICE_CHEST;
			case CORAL  -> PCLootTables.CORAL_CHEST;
			default     -> PCLootTables.NORMAL_CHEST;
		};
	}


	public int getRowCount () {
		return this.size / this.rowLength;
	}

	public BlockEntityType<? extends PCBaseChestBlockEntity> getBlockEntityType () {
		return switch (this) {
			case LUSH   -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.get();
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.get();
			case ROCKY  -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.get();
			case STONE  -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY.get();
			case GOLD   -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY.get();
			case NETHER -> PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY.get();
			case SHADOW -> PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY.get();
			case ICE    -> PCBlockEntities.ICE_CHEST_BLOCK_ENTITY.get();
			case CORAL  -> PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY.get();
			default     -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.get();
		};
	}

	public PCBaseChestBlockEntity makeEntity (BlockPos pos, BlockState state) {
		return switch (this) {
			case LUSH   -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case ROCKY  -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case STONE  -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case GOLD   -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case NETHER -> PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case SHADOW -> PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case ICE    -> PCBlockEntities.ICE_CHEST_BLOCK_ENTITY.get().create(pos, state);
			case CORAL  -> PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY.get().create(pos, state);
			default     -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.get().create(pos, state);
		};
	}

	public MenuType<PCChestScreenHandler> getScreenHandlerType () {
		return switch (this) {
			default -> PCScreenHandlerType.PC_CHEST;
		};
	}

	public BlockBehaviour.Properties setting () {
		return switch (this) {
			case LUSH, NORMAL -> BlockBehaviour.Properties.of()
					.sound(SoundType.WOOD)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.WOOD);
			case ROCKY, STONE -> BlockBehaviour.Properties.of()
					.sound(SoundType.STONE)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.STONE);
			case GOLD, SHADOW -> BlockBehaviour.Properties.of()
					.sound(SoundType.METAL)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.METAL);
			case ICE -> BlockBehaviour.Properties.of()
					.sound(SoundType.GLASS)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.GLASS)
					.lightLevel(state -> 7)
					.friction(0.98f);
			case CORAL -> BlockBehaviour.Properties.of()
					.sound(SoundType.CORAL_BLOCK)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.CORAL_BLOCK)
					.lightLevel(state -> PCChestBlock.isDry(state) ? 0 : 12)
					.friction(0.99f);
			case NETHER -> BlockBehaviour.Properties.of()
					.sound(SoundType.NETHER_BRICKS)
					.destroyTime(2.0F)
					.explosionResistance(3600000.0f)
					.sound(SoundType.STONE)
					.lightLevel(state -> 9);
		};
	}

	public EntityType<? extends PCTameablePetWithInventory> getPetMimicType () {
		return switch (this) {
			case LUSH   -> PCEntities.LUSH_CHEST_MIMIC_PET;
			case ROCKY  -> PCEntities.ROCKY_CHEST_MIMIC_PET;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC_PET;
			case STONE  -> PCEntities.STONE_CHEST_MIMIC_PET;
			case GOLD   -> PCEntities.GOLD_CHEST_MIMIC_PET;
			case NETHER -> PCEntities.NETHER_CHEST_MIMIC_PET;
			case SHADOW -> PCEntities.SHADOW_CHEST_MIMIC_PET;
			case ICE    -> PCEntities.ICE_CHEST_MIMIC_PET;
			case CORAL  -> PCEntities.CORAL_CHEST_MIMIC_PET;
			default     -> PCEntities.NORMAL_CHEST_MIMIC_PET;
		};
	}
}