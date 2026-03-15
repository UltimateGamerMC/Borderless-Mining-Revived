package net.fabricmc.fabric.api.transfer.v1.storage.base;

import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/**
 * A transfer variant storage view that contains a blank variant all the time (it's always empty), but may have a nonzero capacity.
 * This can be used to give capacity hints even if the storage is empty.
 */
public class BlankVariantView<T extends TransferVariant<?>> implements StorageView<T> {
	private final T blankVariant;
	private final long capacity;

	/**
	 * Create a new instance.
	 * @throws IllegalArgumentException If the passed {@code blankVariant} is not blank.
	 */
	public BlankVariantView(T blankVariant, long capacity) {
		if (!blankVariant.isBlank()) {
			throw new IllegalArgumentException("Expected a blank variant, received " + blankVariant);
		}

		this.blankVariant = blankVariant;
		this.capacity = capacity;
	}

	@Override
	public long extract(T resource, long maxAmount, TransactionContext transaction) {
		return 0; // can't extract
	}

	@Override
	public boolean isResourceBlank() {
		return true;
	}

	@Override
	public T getResource() {
		return blankVariant;
	}

	@Override
	public long getAmount() {
		return 0;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}
}
