/*
 * External method calls:
 *   Lnet/minecraft/inventory/Inventory;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/inventory/Inventory;onClose(Lnet/minecraft/entity/ContainerUser;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/HopperScreenHandler;checkSize(Lnet/minecraft/inventory/Inventory;I)V
 *   Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/HopperScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/HopperScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
 */
package net.minecraft.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class HopperScreenHandler
extends ScreenHandler {
    public static final int SLOT_COUNT = 5;
    private final Inventory inventory;

    public HopperScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5));
    }

    public HopperScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.HOPPER, syncId);
        this.inventory = inventory;
        HopperScreenHandler.checkSize(inventory, 5);
        inventory.onOpen(playerInventory.player);
        for (int j = 0; j < 5; ++j) {
            this.addSlot(new Slot(inventory, j, 44 + j * 18, 20));
        }
        this.addPlayerSlots(playerInventory, 8, 51);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack lv = ItemStack.EMPTY;
        Slot lv2 = (Slot)this.slots.get(slot);
        if (lv2 != null && lv2.hasStack()) {
            ItemStack lv3 = lv2.getStack();
            lv = lv3.copy();
            if (slot < this.inventory.size() ? !this.insertItem(lv3, this.inventory.size(), this.slots.size(), true) : !this.insertItem(lv3, 0, this.inventory.size(), false)) {
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

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}

