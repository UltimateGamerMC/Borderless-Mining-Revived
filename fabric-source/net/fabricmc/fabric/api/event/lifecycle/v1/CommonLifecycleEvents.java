package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.minecraft.core.RegistryAccess;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class CommonLifecycleEvents {
	private CommonLifecycleEvents() {
	}

	/**
	 * Called when tags are loaded or updated.
	 */
	public static final Event<TagsLoaded> TAGS_LOADED = EventFactory.createArrayBacked(TagsLoaded.class, callbacks -> (registries, client) -> {
		for (TagsLoaded callback : callbacks) {
			callback.onTagsLoaded(registries, client);
		}
	});

	public interface TagsLoaded {
		/**
		 * @param registries Up-to-date registries from which the tags can be retrieved.
		 * @param client True if the client just received a sync packet, false if the server just (re)loaded the tags.
		 */
		void onTagsLoaded(RegistryAccess registries, boolean client);
	}
}
