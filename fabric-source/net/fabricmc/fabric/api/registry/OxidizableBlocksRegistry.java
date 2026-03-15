package net.fabricmc.fabric.api.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

import net.fabricmc.fabric.impl.content.registry.OxidizableBlocksRegistryImpl;

/**
 * Provides methods for registering oxidizable and waxable blocks.
 */
public final class OxidizableBlocksRegistry {
	private OxidizableBlocksRegistry() {
	}

	/**
	 * Registers a block pair as being able to increase and decrease oxidation.
	 *
	 * @param less the variant with less oxidation
	 * @param more the variant with more oxidation
	 */
	public static void registerOxidizableBlockPair(Block less, Block more) {
		OxidizableBlocksRegistryImpl.registerOxidizableBlockPair(less, more);
	}

	/**
	 * Registers a block pair as being able to add and remove wax.
	 *
	 * @param unwaxed the unwaxed variant
	 * @param waxed   the waxed variant
	 */
	public static void registerWaxableBlockPair(Block unwaxed, Block waxed) {
		OxidizableBlocksRegistryImpl.registerWaxableBlockPair(unwaxed, waxed);
	}

	/**
	 * Registers a {@link WeatheringCopperBlocks} and its oxidizing and waxing variants.
	 *
	 * @param blockSet the copper block set to register
	 */
	public static void registerCopperBlockSet(WeatheringCopperBlocks blockSet) {
		OxidizableBlocksRegistryImpl.registerCopperBlockSet(blockSet);
	}
}
