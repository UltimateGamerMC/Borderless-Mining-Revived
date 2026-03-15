/*
 * External method calls:
 *   Lnet/minecraft/inventory/Inventory;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/screen/slot/Slot;onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/inventory/Inventory;onClose(Lnet/minecraft/entity/ContainerUser;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;checkSize(Lnet/minecraft/inventory/Inventory;I)V
 *   Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;add3x3Slots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
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

public class Generic3x3ContainerScreenHandler
extends ScreenHandler {
    private static final int CONTAINER_SIZE = 9;
    private static final int INVENTORY_START = 9;
    private static final int INVENTORY_END = 36;
    private static final int HOTBAR_START = 36;
    private static final int HOTBAR_END = 45;
    private final Inventory inventory;

    public Generic3x3ContainerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9));
    }

    public Generic3x3ContainerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.GENERIC_3X3, syncId);
        Generic3x3ContainerScreenHandler.checkSize(inventory, 9);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.add3x3Slots(inventory, 62, 17);
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    protected void add3x3Slots(Inventory inventory, int x, int y) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int m = l + k * 3;
                this.addSlot(new Slot(inventory, m, x + l * 18, y + k * 18));
            }
        }
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
            if (slot < 9 ? !this.insertItem(lv3, 9, 45, true) : !this.insertItem(lv3, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if (lv3.isEmpty()) {
                lv2.setStack(ItemStack.EMPTY);
            } else {
                lv2.markDirty();
            }
            if (lv3.getCount() == lv.getCount()) {
                return ItemStack.EMPTY;
            }
            lv2.onTakeItem(player, lv3);
        }
        return lv;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}

