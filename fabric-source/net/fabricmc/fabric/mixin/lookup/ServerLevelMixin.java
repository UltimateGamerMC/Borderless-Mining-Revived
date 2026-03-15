package net.fabricmc.fabric.mixin.lookup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import net.fabricmc.fabric.impl.lookup.block.BlockApiCacheImpl;
import net.fabricmc.fabric.impl.lookup.block.ServerWorldCache;

@Mixin(ServerLevel.class)
abstract class ServerLevelMixin implements ServerWorldCache {
	@Unique
	private final Map<BlockPos, List<WeakReference<BlockApiCacheImpl<?, ?>>>> apiLookupCaches = new Object2ReferenceOpenHashMap<>();
	/**
	 * Ensures that the apiLookupCaches map is iterated over every once in a while to clean up caches.
	 */
	@Unique
	private int apiLookupAccessesWithoutCleanup = 0;

	@Override
	public void fabric_registerCache(BlockPos pos, BlockApiCacheImpl<?, ?> cache) {
		List<WeakReference<BlockApiCacheImpl<?, ?>>> caches = apiLookupCaches.computeIfAbsent(pos.immutable(), ignored -> new ArrayList<>());
		caches.removeIf(weakReference -> weakReference.get() == null);
		caches.add(new WeakReference<>(cache));
		apiLookupAccessesWithoutCleanup++;
	}

	@Override
	public void fabric_invalidateCache(BlockPos pos) {
		List<WeakReference<BlockApiCacheImpl<?, ?>>> caches = apiLookupCaches.get(pos);

		if (caches != null) {
			caches.removeIf(weakReference -> weakReference.get() == null);

			if (caches.size() == 0) {
				apiLookupCaches.remove(pos);
			} else {
				caches.forEach(weakReference -> {
					BlockApiCacheImpl<?, ?> cache = weakReference.get();

					if (cache != null) {
						cache.invalidate();
					}
				});
			}
		}

		apiLookupAccessesWithoutCleanup++;

		// Try to invalidate GC'd lookups from the cache after 2 * the number of cached lookups
		if (apiLookupAccessesWithoutCleanup > 2 * apiLookupCaches.size()) {
			apiLookupCaches.entrySet().removeIf(entry -> {
				entry.getValue().removeIf(weakReference -> weakReference.get() == null);
				return entry.getValue().isEmpty();
			});

			apiLookupAccessesWithoutCleanup = 0;
		}
	}
}
