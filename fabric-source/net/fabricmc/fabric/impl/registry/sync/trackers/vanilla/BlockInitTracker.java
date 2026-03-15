package net.fabricmc.fabric.impl.registry.sync.trackers.vanilla;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.mixin.registry.sync.DebugLevelSourceAccessor;

public final class BlockInitTracker {
	public static void postFreeze() {
		final List<BlockState> blockStateList = BuiltInRegistries.BLOCK.stream()
				.flatMap((block) -> block.getStateDefinition().getPossibleStates().stream())
				.toList();

		final int xLength = Mth.ceil(Mth.sqrt(blockStateList.size()));
		final int zLength = Mth.ceil(blockStateList.size() / (float) xLength);

		DebugLevelSourceAccessor.setALL_BLOCKS(blockStateList);
		DebugLevelSourceAccessor.setGRID_WIDTH(xLength);
		DebugLevelSourceAccessor.setGRID_HEIGHT(zLength);
	}
}
