package net.fabricmc.fabric.mixin.biome.modification;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.RegistryAccess;

import net.fabricmc.fabric.impl.biome.modification.BiomeModificationMarker;

/**
 * This Mixin allows us to keep backup copies of biomes for
 * {@link net.fabricmc.fabric.impl.biome.modification.BiomeModificationImpl} on a per-DynamicRegistryManager basis.
 */
@Mixin(RegistryAccess.ImmutableRegistryAccess.class)
public class RegistryAccessImmutableRegistryAccessMixin implements BiomeModificationMarker {
	@Unique
	private boolean modified;

	@Override
	public void fabric_markModified() {
		if (modified) {
			throw new IllegalStateException("This dynamic registries instance has already been modified");
		}

		modified = true;
	}
}
