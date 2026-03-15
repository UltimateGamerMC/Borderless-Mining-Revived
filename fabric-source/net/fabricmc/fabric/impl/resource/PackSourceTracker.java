package net.fabricmc.fabric.impl.resource;

import java.util.WeakHashMap;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackSource;

import net.fabricmc.fabric.api.resource.v1.FabricResource;

/**
 * Tracks the sources of resource packs in a global weak hash map.
 * {@link PackResources} doesn't hold a reference to its {@link PackSource}
 * so we store the source in the map when the resource packs are created.
 * See {@link net.fabricmc.fabric.mixin.resource.v1.PackMixin PackMixin}.
 *
 * <p>The sources are later read for use in {@link FabricResource}.
 */
public final class PackSourceTracker {
	// Use a weak hash map so that if resource packs would be deleted, this won't keep them alive.
	private static final WeakHashMap<PackResources, PackSource> SOURCES = new WeakHashMap<>();

	/**
	 * Gets the source of a pack.
	 *
	 * @param pack the resource pack
	 * @return the source, or {@link PackSource#NONE} if not tracked
	 */
	public static PackSource getSource(PackResources pack) {
		return SOURCES.getOrDefault(pack, PackSource.DEFAULT);
	}

	/**
	 * Sets the source of a pack.
	 *
	 * @param pack the resource pack
	 * @param source the source
	 */
	public static void setSource(PackResources pack, PackSource source) {
		SOURCES.put(pack, source);
	}
}
