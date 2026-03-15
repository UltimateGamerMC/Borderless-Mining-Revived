package net.fabricmc.fabric.impl.transfer.item;

import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/**
 * Internal class that allows inventory instances to defer special logic until {@link InventorySlotWrapper#onFinalCommit()} is called.
 */
public interface SpecialLogicInventory {
	/**
	 * Decide whether special logic should now be suppressed. If true, must remain suppressed until the next call.
	 */
	void fabric_setSuppress(boolean suppress);

	void fabric_onFinalCommit(int slot, ItemStack oldStack, ItemStack newStack);

	/**
	 * Called after a slot has been modified (i.e. insert or extract with result > 0).
	 */
	default void fabric_onTransfer(int slot, TransactionContext transaction) {
	}
}
