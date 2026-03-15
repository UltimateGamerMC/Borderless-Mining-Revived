package net.fabricmc.fabric.api.resource.v1.pack;

import net.minecraft.server.packs.PackResources;

import net.fabricmc.loader.api.metadata.ModMetadata;

/**
 * Interface implemented by mod-provided resource packs.
 */
public interface ModPackResources extends PackResources {
	/**
	 * {@return the metadata associated with the mod providing this resource pack}
	 */
	ModMetadata getFabricModMetadata();

	ModPackResources createOverlay(String overlay);
}
