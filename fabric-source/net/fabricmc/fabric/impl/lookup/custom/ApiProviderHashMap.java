package net.fabricmc.fabric.impl.lookup.custom;

import java.util.Map;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.custom.ApiProviderMap;

public final class ApiProviderHashMap<K, V> implements ApiProviderMap<K, V> {
	private volatile Map<K, V> lookups = new Reference2ReferenceOpenHashMap<>();

	@Nullable
	@Override
	public V get(K key) {
		Objects.requireNonNull(key, "Key may not be null.");

		return lookups.get(key);
	}

	@Override
	public synchronized V putIfAbsent(K key, V provider) {
		Objects.requireNonNull(key, "Key may not be null.");
		Objects.requireNonNull(provider, "Provider may not be null.");

		// We use a copy-on-write strategy to allow any number of reads to concur with a write
		Map<K, V> lookupsCopy = new Reference2ReferenceOpenHashMap<>(lookups);
		V result = lookupsCopy.putIfAbsent(key, provider);
		lookups = lookupsCopy;

		return result;
	}
}
