package net.fabricmc.fabric.impl.registry.sync;

import net.minecraft.core.Registry;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

public interface ListenableRegistry<T> {
	Event<RegistryEntryAddedCallback<T>> fabric_getAddObjectEvent();
	Event<RegistryIdRemapCallback<T>> fabric_getRemapEvent();
	@SuppressWarnings("unchecked")
	static <T> ListenableRegistry<T> get(Registry<T> registry) {
		if (!(registry instanceof ListenableRegistry)) {
			throw new IllegalArgumentException("Unsupported registry: " + registry.key().identifier());
		}

		// Safe cast: this is implemented via Mixin and T will always match the T in Registry<T>
		return (ListenableRegistry<T>) registry;
	}
}
