package net.fabricmc.fabric.api.registry;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;

/**
 * A registry for shovel flattening interactions. A vanilla example is turning dirt to dirt paths.
 */
public final class FlattenableBlockRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlattenableBlockRegistry.class);

	private FlattenableBlockRegistry() {
	}

	/**
	 * Registers a flattening interaction.
	 *
	 * @param input     the input block that can be flattened
	 * @param flattened the flattened result block state
	 */
	public static void register(Block input, BlockState flattened) {
		Objects.requireNonNull(input, "input block cannot be null");
		Objects.requireNonNull(flattened, "flattened block state cannot be null");
		BlockState old = ShovelItemAccessor.getPathStates().put(input, flattened);

		if (old != null) {
			LOGGER.debug("Replaced old flattening mapping from {} to {} with {}", input, old, flattened);
		}
	}
}
