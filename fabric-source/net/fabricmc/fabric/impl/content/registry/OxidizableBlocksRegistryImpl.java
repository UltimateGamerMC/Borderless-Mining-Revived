package net.fabricmc.fabric.impl.content.registry;

import java.util.Objects;

import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

public final class OxidizableBlocksRegistryImpl {
	private OxidizableBlocksRegistryImpl() {
	}

	public static void registerOxidizableBlockPair(Block less, Block more) {
		Objects.requireNonNull(less, "Oxidizable block cannot be null!");
		Objects.requireNonNull(more, "Oxidizable block cannot be null!");
		WeatheringCopper.NEXT_BY_BLOCK.get().put(less, more);
		// Fix #4371
		refreshRandomTickCache(less);
		refreshRandomTickCache(more);
	}

	public static void registerWaxableBlockPair(Block unwaxed, Block waxed) {
		Objects.requireNonNull(unwaxed, "Unwaxed block cannot be null!");
		Objects.requireNonNull(waxed, "Waxed block cannot be null!");
		HoneycombItem.WAXABLES.get().put(unwaxed, waxed);
	}

	public static void registerCopperBlockSet(WeatheringCopperBlocks blockSet) {
		Objects.requireNonNull(blockSet, "blockSet cannot be null!");
		blockSet.weatheringMapping().forEach(OxidizableBlocksRegistryImpl::registerOxidizableBlockPair);
		blockSet.waxedMapping().forEach(OxidizableBlocksRegistryImpl::registerWaxableBlockPair);
	}

	private static void refreshRandomTickCache(Block block) {
		block.getStateDefinition().getPossibleStates().forEach(state -> ((RandomTickCacheRefresher) state).fabric_api$refreshRandomTickCache());
	}

	public interface RandomTickCacheRefresher {
		void fabric_api$refreshRandomTickCache();
	}
}
