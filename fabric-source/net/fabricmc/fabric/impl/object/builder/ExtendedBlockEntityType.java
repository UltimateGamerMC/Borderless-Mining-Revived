package net.fabricmc.fabric.impl.object.builder;

import java.util.Set;

import org.jspecify.annotations.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExtendedBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> {
	@Nullable
	private final Boolean canPotentiallyExecuteCommands;

	public ExtendedBlockEntityType(BlockEntitySupplier<? extends T> factory, Set<Block> blocks, @Nullable Boolean canPotentiallyExecuteCommands) {
		super(factory, blocks);
		this.canPotentiallyExecuteCommands = canPotentiallyExecuteCommands;
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		if (canPotentiallyExecuteCommands != null) {
			return canPotentiallyExecuteCommands;
		}

		return super.onlyOpCanSetNbt();
	}
}
