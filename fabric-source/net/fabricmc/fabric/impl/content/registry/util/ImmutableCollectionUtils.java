package net.fabricmc.fabric.impl.content.registry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImmutableCollectionUtils {
	public static <T> Set<T> getAsMutableSet(Supplier<Set<T>> getter, Consumer<Set<T>> setter) {
		Set<T> set = getter.get();

		if (!(set instanceof HashSet)) {
			setter.accept(set = new HashSet<>(set));
		}

		return set;
	}

	public static <T> List<T> getAsMutableList(Supplier<List<T>> getter, Consumer<List<T>> setter) {
		List<T> set = getter.get();

		if (!(set instanceof ArrayList)) {
			setter.accept(set = new ArrayList<>(set));
		}

		return set;
	}

	public static <K, V> Map<K, V> getAsMutableMap(Supplier<Map<K, V>> getter, Consumer<Map<K, V>> setter) {
		Map<K, V> map = getter.get();

		if (!(map instanceof HashMap)) {
			setter.accept(map = new HashMap<>(map));
		}

		return map;
	}
}
