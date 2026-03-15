package net.fabricmc.fabric.impl.transfer.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

/**
 * Sidedness-aware wrapper around a {@link InventoryStorageImpl} for sided inventories.
 */
class SidedInventoryStorageImpl extends CombinedStorage<ItemVariant, SingleSlotStorage<ItemVariant>> implements InventoryStorage {
	private final InventoryStorageImpl backingStorage;

	SidedInventoryStorageImpl(InventoryStorageImpl storage, Direction direction) {
		super(Collections.unmodifiableList(createWrapperList(storage, direction)));
		this.backingStorage = storage;
	}

	@Override
	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		return parts;
	}

	private static List<SingleSlotStorage<ItemVariant>> createWrapperList(InventoryStorageImpl storage, Direction direction) {
		WorldlyContainer inventory = (WorldlyContainer) storage.inventory;
		int[] availableSlots = inventory.getSlotsForFace(direction);
		SidedInventorySlotWrapper[] slots = new SidedInventorySlotWrapper[availableSlots.length];

		for (int i = 0; i < availableSlots.length; ++i) {
			slots[i] = new SidedInventorySlotWrapper(storage.backingList.get(availableSlots[i]), inventory, direction);
		}

		return Arrays.asList(slots);
	}

	@Override
	public String toString() {
		// These two are the same from the user's perspective.
		return backingStorage.toString();
	}
}
