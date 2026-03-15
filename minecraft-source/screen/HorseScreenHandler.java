/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;createEquipmentInventory(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/inventory/Inventory;
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;areInventoriesDifferent(Lnet/minecraft/inventory/Inventory;)Z
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/HorseScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/HorseScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 */
package net.minecraft.screen;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.screen.MountScreenHandler;
import net.minecraft.screen.slot.ArmorSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class HorseScreenHandler
extends MountScreenHandler {
    private static final Identifier EMPTY_SADDLE_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/saddle");
    private static final Identifier EMPTY_LLAMA_ARMOR_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/llama_armor");
    private static final Identifier EMPTY_HORSE_ARMOR_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/horse_armor");

    public HorseScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, final AbstractHorseEntity entity, int slotColumnCount) {
        super(syncId, playerInventory, inventory, entity);
        Inventory lv = entity.createEquipmentInventory(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(this, lv, entity, EquipmentSlot.SADDLE, 0, 8, 18, EMPTY_SADDLE_SLOT_TEXTURE){

            @Override
            public boolean isEnabled() {
                return entity.canUseSlot(EquipmentSlot.SADDLE) && entity.getType().isIn(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        final boolean bl = entity instanceof LlamaEntity;
        Identifier lv2 = bl ? EMPTY_LLAMA_ARMOR_SLOT_TEXTURE : EMPTY_HORSE_ARMOR_SLOT_TEXTURE;
        Inventory lv3 = entity.createEquipmentInventory(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(this, lv3, entity, EquipmentSlot.BODY, 0, 8, 36, lv2){

            @Override
            public boolean isEnabled() {
                return entity.canUseSlot(EquipmentSlot.BODY) && (entity.getType().isIn(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || bl);
            }
        });
        if (slotColumnCount > 0) {
            for (int k = 0; k < 3; ++k) {
                for (int l = 0; l < slotColumnCount; ++l) {
                    this.addSlot(new Slot(inventory, l + k * slotColumnCount, 80 + l * 18, 18 + k * 18));
                }
            }
        }
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    protected boolean areInventoriesDifferent(Inventory inventory) {
        return ((AbstractHorseEntity)this.mount).areInventoriesDifferent(inventory);
    }
}

