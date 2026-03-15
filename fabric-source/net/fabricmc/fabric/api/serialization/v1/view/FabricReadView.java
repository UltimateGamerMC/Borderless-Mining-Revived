package net.fabricmc.fabric.api.serialization.v1.view;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.level.storage.ValueInput;

import net.fabricmc.fabric.impl.serialization.SpecialCodecs;

/**
 * Fabric provided extension of ReadView.
 *
 * <p>Note: This interface is automatically implemented on {@link ValueInput} via Mixin and interface injection.
 */
public interface FabricReadView {
	/**
	 * Returns a collection of keys available in this {@link ValueInput}.
	 *
	 * @return collection of keys or empty list if this {@link ValueInput} is empty.
	 */
	default Collection<String> keys() {
		//noinspection deprecation
		return ((ValueInput) this).read(SpecialCodecs.KEYS_EXTRACT).orElse(List.of());
	}

	/**
	 * Checks if this {@link ValueInput} contains data under provided key.
	 *
	 * @param key key to check for
	 * @return true, when this {@link ValueInput} contains data under provided key, otherwise false
	 */
	default boolean contains(String key) {
		return ((ValueInput) this).read(SpecialCodecs.contains(key)).orElseThrow();
	}

	/**
	 * Returns an long array present in this {@link ValueInput} under provided key.
	 *
	 * @param key key to check for
	 * @return long array wrapped in optional if long array is present, empty Optional otherwise
	 */
	default Optional<long[]> getOptionalLongArray(String key) {
		return ((ValueInput) this).read(key, SpecialCodecs.LONG_ARRAY);
	}

	/**
	 * Returns an byte array present in this {@link ValueInput} under provided key.
	 *
	 * @param key key to check for
	 * @return byte array wrapped in optional if byte array is present, empty Optional otherwise
	 */
	default Optional<byte[]> getOptionalByteArray(String key) {
		return ((ValueInput) this).read(key, SpecialCodecs.BYTE_ARRAY);
	}
}
