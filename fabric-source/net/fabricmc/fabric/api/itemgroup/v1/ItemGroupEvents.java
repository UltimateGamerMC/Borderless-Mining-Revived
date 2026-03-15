package net.fabricmc.fabric.api.itemgroup.v1;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupEventsImpl;

/**
 * Holds events related to {@link CreativeModeTabs}.
 */
public final class ItemGroupEvents {
	private ItemGroupEvents() {
	}

	/**
	 * This event allows the entries of any item group to be modified.
	 * <p/>
	 * Use {@link #modifyEntriesEvent(ResourceKey)} to get the event for a specific item group.
	 * <p/>
	 * This event is invoked after those two more specific events.
	 */
	public static final Event<ModifyEntriesAll> MODIFY_ENTRIES_ALL = EventFactory.createArrayBacked(ModifyEntriesAll.class, callbacks -> (group, entries) -> {
		for (ModifyEntriesAll callback : callbacks) {
			callback.modifyEntries(group, entries);
		}
	});

	/**
	 * Returns the modify entries event for a specific item group. This uses the group ID and
	 * is suitable for modifying a modded item group that might not exist.
	 * @param registryKey the {@link ResourceKey} of the item group to modify
	 * @return the event
	 */
	public static Event<ModifyEntries> modifyEntriesEvent(ResourceKey<CreativeModeTab> registryKey) {
		return ItemGroupEventsImpl.getOrCreateModifyEntriesEvent(registryKey);
	}

	@FunctionalInterface
	public interface ModifyEntries {
		/**
		 * Modifies the item group entries.
		 * @param entries the entries
		 * @see FabricItemGroupEntries
		 */
		void modifyEntries(FabricItemGroupEntries entries);
	}

	@FunctionalInterface
	public interface ModifyEntriesAll {
		/**
		 * Modifies the item group entries.
		 * @param group the item group that is being modified
		 * @param entries the entries
		 * @see FabricItemGroupEntries
		 */
		void modifyEntries(CreativeModeTab group, FabricItemGroupEntries entries);
	}
}
