package net.fabricmc.fabric.impl.resource;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface FabricResourceReloader extends PreparableReloadListener {
	/**
	 * {@return the unique identifier of this Vanilla resource reloader}
	 */
	Identifier fabric$getId();

	@Override
	default String getName() {
		// Give a more descriptive name to Vanilla resource reloaders
		// as in production their intermediary class names are not meaningful
		// when profiling.
		return this.fabric$getId() + " (" + this.getClass().getSimpleName() + ")";
	}
}
