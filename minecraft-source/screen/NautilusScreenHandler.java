/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;createEquipmentInventory(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/inventory/Inventory;
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;areInventoriesDifferent(Lnet/minecraft/inventory/Inventory;)Z
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/NautilusScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/NautilusScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 */
package net.minecraft.screen;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractNautilusEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.MountScreenHandler;
import net.minecraft.screen.slot.ArmorSlot;
import net.minecraft.util.Identifier;

public class NautilusScreenHandler
extends MountScreenHandler {
    private static final Identifier EMPTY_SADDLE_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/saddle");
    private static final Identifier EMPTY_NAUTILUS_ARMOR_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/nautilus_armor_inventory");

    public NautilusScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, final AbstractNautilusEntity nautilus, int slotColumnCount) {
        super(syncId, playerInventory, inventory, nautilus);
        Inventory lv = nautilus.createEquipmentInventory(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(this, lv, nautilus, EquipmentSlot.SADDLE, 0, 8, 18, EMPTY_SADDLE_SLOT_TEXTURE){

            @Override
            public boolean isEnabled() {
                return nautilus.canUseSlot(EquipmentSlot.SADDLE);
            }
        });
        Inventory lv2 = nautilus.createEquipmentInventory(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(this, lv2, nautilus, EquipmentSlot.BODY, 0, 8, 36, EMPTY_NAUTILUS_ARMOR_SLOT_TEXTURE){

            @Override
            public boolean isEnabled() {
                return nautilus.canUseSlot(EquipmentSlot.BODY);
            }
        });
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    protected boolean areInventoriesDifferent(Inventory inventory) {
        return ((AbstractNautilusEntity)this.mount).areInventoriesDifferent(inventory);
    }
}

