package net.fabricmc.fabric.impl.biome.modification;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Utility class for accessing the worldgen data that vanilla uses to generate its vanilla datapack.
 */
public final class BuiltInRegistryKeys {
	private static final HolderLookup.Provider vanillaRegistries = VanillaRegistries.createLookup();

	private BuiltInRegistryKeys() {
	}

	public static boolean isBuiltinBiome(ResourceKey<Biome> key) {
		return biomeRegistryWrapper().get(key).isPresent();
	}

	public static HolderGetter<Biome> biomeRegistryWrapper() {
		return vanillaRegistries.lookupOrThrow(Registries.BIOME);
	}
}
