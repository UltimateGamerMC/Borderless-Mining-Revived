package net.fabricmc.fabric.api.command.v2;

import net.minecraft.resources.Identifier;

/**
 * Fabric extension to {@link net.minecraft.commands.arguments.selector.EntitySelectorParser}, implemented
 * using interface injection. This allows custom entity selectors to
 * set a custom flag to a reader. This can be used to implement mutually-exclusive
 * or non-repeatable entity selector option.
 */
public interface FabricEntitySelectorReader {
	/**
	 * Sets a flag.
	 * @param key the key of the flag
	 * @param value the value of the flag
	 */
	default void setCustomFlag(Identifier key, boolean value) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Gets the value of the flag.
	 * @param key the key of the flag
	 * @return the value, or {@code false} if the flag is not set
	 */
	default boolean getCustomFlag(Identifier key) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
