/*
 * External method calls:
 *   Lnet/minecraft/inventory/Inventory;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/inventory/Inventory;onClose(Lnet/minecraft/entity/ContainerUser;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/MountScreenHandler;areInventoriesDifferent(Lnet/minecraft/inventory/Inventory;)Z
 *   Lnet/minecraft/screen/MountScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
 */
package net.minecraft.screen;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public abstract class MountScreenHandler
extends ScreenHandler {
    protected final Inventory inventory;
    protected final LivingEntity mount;
    protected final int field_64487 = 0;
    protected final int field_64488 = 1;
    protected final int field_64489 = 2;
    protected static final int field_64490 = 3;

    protected MountScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, LivingEntity mount) {
        super(null, syncId);
        this.inventory = inventory;
        this.mount = mount;
        inventory.onOpen(playerInventory.player);
    }

    protected abstract boolean areInventoriesDifferent(Inventory var1);

    @Override
    public boolean canUse(PlayerEntity player) {
        return !this.areInventoriesDifferent(this.inventory) && this.inventory.canPlayerUse(player) && this.mount.isAlive() && player.canInteractWithEntity(this.mount, 4.0);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack lv = ItemStack.EMPTY;
        Slot lv2 = (Slot)this.slots.get(slot);
        if (lv2 != null && lv2.hasStack()) {
            ItemStack lv3 = lv2.getStack();
            lv = lv3.copy();
            int j = 2 + this.inventory.size();
            if (slot < j) {
                if (!this.insertItem(lv3, j, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).canInsert(lv3) && !this.getSlot(1).hasStack()) {
                if (!this.insertItem(lv3, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).canInsert(lv3) && !this.getSlot(0).hasStack()) {
                if (!this.insertItem(lv3, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inventory.size() == 0 || !this.insertItem(lv3, 2, j, false)) {
                int k;
                int l = k = j + 27;
                int m = l + 9;
                if (slot >= l && slot < m ? !this.insertItem(lv3, j, k, false) : (slot >= j && slot < k ? !this.insertItem(lv3, l, m, false) : !this.insertItem(lv3, l, k, false))) {
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (lv3.isEmpty()) {
                lv2.setStack(ItemStack.EMPTY);
            } else {
                lv2.markDirty();
            }
        }
        return lv;
    }

    public static int getSlotCount(int columns) {
        return columns * 3;
    }
}

