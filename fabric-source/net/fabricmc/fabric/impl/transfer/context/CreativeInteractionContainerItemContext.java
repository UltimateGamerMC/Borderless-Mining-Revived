package net.fabricmc.fabric.impl.transfer.context;

import net.minecraft.world.entity.player.Player;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class CreativeInteractionContainerItemContext extends ConstantContainerItemContext {
	private final PlayerInventoryStorage playerInventory;

	public CreativeInteractionContainerItemContext(ItemVariant initialVariant, long initialAmount, Player player) {
		super(initialVariant, initialAmount);

		this.playerInventory = PlayerInventoryStorage.of(player);
	}

	@Override
	public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
		StoragePreconditions.notBlankNotNegative(itemVariant, maxAmount);

		if (maxAmount > 0) {
			// Only add the item to the player inventory if it's not already in the inventory.
			boolean hasItem = false;

			for (SingleSlotStorage<ItemVariant> slot : playerInventory.getSlots()) {
				if (slot.getResource().equals(itemVariant) && slot.getAmount() > 0) {
					hasItem = true;
					break;
				}
			}

			if (!hasItem) {
				playerInventory.offer(itemVariant, 1, transactionContext);
			}
		}

		// Insertion always succeeds from the POV of the context user.
		return maxAmount;
	}

	@Override
	public String toString() {
		return "CreativeInteractionContainerItemContext[%d %s]"
				.formatted(getMainSlot().getAmount(), getMainSlot().getResource());
	}
}
