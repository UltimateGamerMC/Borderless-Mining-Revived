package net.fabricmc.fabric.api.item.v1;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.component.DataComponentType;

/**
 * Fabric-provided extensions for {@link net.minecraft.core.component.DataComponentMap.Builder}.
 *
 * <p>Note: This interface is automatically implemented on all component map builders via Mixin and interface injection.
 */
@ApiStatus.NonExtendable
public interface FabricComponentMapBuilder {
	/**
	 * Gets the current value for the component type in the builder, or creates and adds a new value if it is not present.
	 *
	 * @param type     The component type
	 * @param fallback The supplier for the default data value if the type is not in this map yet. The value given by this supplier
	 *                 may not be null.
	 * @param <T>      The type of the component data
	 * @return Returns the current value in the map builder, or the default value provided by the fallback if not present
	 * @see #getOrEmpty(DataComponentType)
	 */
	default <T> T getOrCreate(DataComponentType<T> type, Supplier<T> fallback) {
		throw new AssertionError("Implemented in Mixin");
	}

	/**
	 * Gets the current value for the component type in the builder, or creates and adds a new value if it is not present.
	 *
	 * @param type         The component type
	 * @param defaultValue The default data value if the type is not in this map yet
	 * @param <T>          The type of the component data
	 * @return Returns the current value in the map builder, or the default value if not present
	 */
	default <T> T getOrDefault(DataComponentType<T> type, T defaultValue) {
		Objects.requireNonNull(defaultValue, "Cannot insert null values to component map builder");
		return getOrCreate(type, () -> defaultValue);
	}

	/**
	 * For list component types specifically, returns a mutable list of values currently held in the builder for the given
	 * component type. If the type is not registered to this builder yet, this will create and add a new empty list to the builder
	 * for the type, and return that.
	 *
	 * @param type The component type. The component must be a list-type.
	 * @param <T>  The type of the component entry data
	 * @return Returns a mutable list of values for the type.
	 */
	default <T> List<T> getOrEmpty(DataComponentType<List<T>> type) {
		throw new AssertionError("Implemented in Mixin");
	}

	/**
	 * Checks if a component type has been registered to this builder.
	 *
	 * @param type The component type to check
	 * @return Returns true if the type has been registered to this builder, false otherwise
	 */
	default boolean contains(DataComponentType<?> type) {
		throw new AssertionError("Implemented in Mixin");
	}
}
