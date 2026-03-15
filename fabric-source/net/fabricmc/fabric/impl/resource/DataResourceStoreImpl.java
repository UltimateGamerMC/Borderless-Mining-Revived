package net.fabricmc.fabric.impl.resource;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import net.fabricmc.fabric.api.resource.v1.DataResourceStore;

public final class DataResourceStoreImpl implements DataResourceStore.Mutable {
	private final Map<Key<?>, Object> store = new IdentityHashMap<>();

	@Override
	public <T> void put(Key<T> key, T data) {
		this.store.put(key, data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrThrow(Key<T> key) {
		return Objects.requireNonNull((T) this.store.get(key));
	}
}
