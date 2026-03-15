package net.fabricmc.fabric.impl.transfer.context;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class ConstantContainerItemContext implements ContainerItemContext {
	private final SingleVariantStorage<ItemVariant> backingSlot = new SingleVariantStorage<>() {
		@Override
		protected ItemVariant getBlankVariant() {
			return ItemVariant.blank();
		}

		@Override
		protected long getCapacity(ItemVariant variant) {
			return Long.MAX_VALUE;
		}

		@Override
		public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);

			// Pretend we can't insert anything to route every insertion through insertOverflow.
			return 0;
		}

		@Override
		public long extract(ItemVariant extractedVariant, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);

			// Pretend we can extract anything, but never actually do it.
			return maxAmount;
		}
	};

	public ConstantContainerItemContext(ItemVariant initialVariant, long initialAmount) {
		backingSlot.variant = initialVariant;
		backingSlot.amount = initialAmount;
	}

	@Override
	public SingleSlotStorage<ItemVariant> getMainSlot() {
		return backingSlot;
	}

	@Override
	public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
		StoragePreconditions.notBlankNotNegative(itemVariant, maxAmount);
		// Always allow anything to be inserted.
		return maxAmount;
	}

	@Override
	public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return "ConstantContainerItemContext[%d %s]"
				.formatted(getMainSlot().getAmount(), getMainSlot().getResource());
	}
}
