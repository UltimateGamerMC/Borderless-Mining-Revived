package net.fabricmc.fabric.impl.biome.modification;

/**
 * Prevents double-modification of biomes in the same dynamic registry manager from occurring and fails-fast
 * if it does occur.
 */
public interface BiomeModificationMarker {
	void fabric_markModified();
}
