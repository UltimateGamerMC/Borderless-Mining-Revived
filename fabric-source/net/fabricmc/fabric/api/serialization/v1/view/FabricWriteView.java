package net.fabricmc.fabric.api.serialization.v1.view;

import net.minecraft.world.level.storage.ValueOutput;

import net.fabricmc.fabric.impl.serialization.SpecialCodecs;

/**
 * Fabric provided extension of WriteView.
 *
 * <p>Note: This interface is automatically implemented on {@link ValueOutput} via Mixin and interface injection.
 */
public interface FabricWriteView {
	default void putLongArray(String key, long[] value) {
		((ValueOutput) this).store(key, SpecialCodecs.LONG_ARRAY, value);
	}

	default void putByteArray(String key, byte[] value) {
		((ValueOutput) this).store(key, SpecialCodecs.BYTE_ARRAY, value);
	}
}
