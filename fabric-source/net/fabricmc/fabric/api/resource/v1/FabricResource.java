package net.fabricmc.fabric.api.resource.v1;

import org.slf4j.LoggerFactory;

import net.minecraft.server.packs.repository.PackSource;

/**
 * Extensions to {@link net.minecraft.server.packs.resources.Resource}.
 * Automatically implemented there via a mixin.
 */
public interface FabricResource {
	/**
	 * Gets the resource pack source of this resource.
	 * The source is used to separate vanilla/mod resources from user resources in Fabric API.
	 *
	 * <p>Custom {@link net.minecraft.server.packs.resources.Resource} implementations should override this method.
	 *
	 * @return the resource pack source
	 */
	default PackSource getFabricPackSource() {
		LoggerFactory.getLogger(FabricResource.class).error("Unknown Resource implementation {}, returning PACK_SOURCE_NONE as the source", this.getClass().getName());
		return PackSource.DEFAULT;
	}
}
