package net.fabricmc.fabric.api.object.builder.v1.block.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.mojang.datafixers.types.Type;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.impl.object.builder.ExtendedBlockEntityType;

/**
 * Use this builder to create a {@link BlockEntityType}.
 */
public final class FabricBlockEntityTypeBuilder<T extends BlockEntity> {
	private final Factory<? extends T> factory;
	private final Set<Block> blocks = new HashSet<>();
	@Nullable
	private Boolean canPotentiallyExecuteCommands = null;

	private FabricBlockEntityTypeBuilder(Factory<? extends T> factory) {
		this.factory = factory;
	}

	public static <T extends BlockEntity> FabricBlockEntityTypeBuilder<T> create(Factory<? extends T> factory, Block... blocks) {
		return new FabricBlockEntityTypeBuilder<T>(factory).addBlocks(blocks);
	}

	/**
	 * Adds a supported block for the block entity type.
	 *
	 * @param block the supported block
	 * @return this builder
	 */
	public FabricBlockEntityTypeBuilder<T> addBlock(Block block) {
		this.blocks.add(block);
		return this;
	}

	/**
	 * Adds supported blocks for the block entity type.
	 *
	 * @param blocks the supported blocks
	 * @return this builder
	 */
	public FabricBlockEntityTypeBuilder<T> addBlocks(Block... blocks) {
		Collections.addAll(this.blocks, blocks);
		return this;
	}

	/**
	 * Adds supported blocks for the block entity type.
	 *
	 * @param blocks the supported blocks
	 * @return this builder
	 */
	public FabricBlockEntityTypeBuilder<T> addBlocks(Collection<? extends Block> blocks) {
		this.blocks.addAll(blocks);
		return this;
	}

	/**
	 * Makes the built {@link BlockEntityType} return {@code true} from
	 * {@link BlockEntityType#onlyOpCanSetNbt()}.
	 *
	 * @param canPotentiallyExecuteCommands whether the block entity is able to execute commands
	 * @return this builder
	 */
	public FabricBlockEntityTypeBuilder<T> canPotentiallyExecuteCommands(boolean canPotentiallyExecuteCommands) {
		this.canPotentiallyExecuteCommands = canPotentiallyExecuteCommands;
		return this;
	}

	public BlockEntityType<T> build() {
		return new ExtendedBlockEntityType<>(factory::create, new HashSet<>(blocks), canPotentiallyExecuteCommands);
	}

	/**
	 * @deprecated Use {@link #build()} instead.
	 */
	@Deprecated
	public BlockEntityType<T> build(@Nullable Type<?> type) {
		return build();
	}

	@FunctionalInterface
	public interface Factory<T extends BlockEntity> {
		T create(BlockPos blockPos, BlockState blockState);
	}
}
