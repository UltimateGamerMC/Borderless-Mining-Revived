/*
 * External method calls:
 *   Lnet/minecraft/inventory/Inventory;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/inventory/Inventory;onClose(Lnet/minecraft/entity/ContainerUser;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/ShulkerBoxScreenHandler;checkSize(Lnet/minecraft/inventory/Inventory;I)V
 *   Lnet/minecraft/screen/ShulkerBoxScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/ShulkerBoxScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/ShulkerBoxScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
 */
package net.minecraft.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;

public class ShulkerBoxScreenHandler
extends ScreenHandler {
    private static final int INVENTORY_SIZE = 27;
    private final Inventory inventory;

    public ShulkerBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27));
    }

    public ShulkerBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.SHULKER_BOX, syncId);
        ShulkerBoxScreenHandler.checkSize(inventory, 27);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        int j = 3;
        int k = 9;
        for (int l = 0; l < 3; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new ShulkerBoxSlot(inventory, m + l * 9, 8 + m * 18, 18 + l * 18));
            }
        }
        this.addPlayerSlots(playerInventory, 8, 84);
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

