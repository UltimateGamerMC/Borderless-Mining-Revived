/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/MobEntity;triggerItemPickedUpByEntityCriteria(Lnet/minecraft/entity/ItemEntity;)V
 *   Lnet/minecraft/inventory/SimpleInventory;addStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/mob/MobEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V
 *   Lnet/minecraft/inventory/SimpleInventory;toDataList(Lnet/minecraft/storage/WriteView$ListAppender;)V
 *   Lnet/minecraft/inventory/SimpleInventory;readDataList(Lnet/minecraft/storage/ReadView$TypedListReadView;)V
 */
package net.minecraft.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public interface InventoryOwner {
    public static final String INVENTORY_KEY = "Inventory";

    public SimpleInventory getInventory();

    public static void pickUpItem(ServerWorld world, MobEntity entity, InventoryOwner inventoryOwner, ItemEntity item) {
        ItemStack lv = item.getStack();
        if (entity.canGather(world, lv)) {
            SimpleInventory lv2 = inventoryOwner.getInventory();
            boolean bl = lv2.canInsert(lv);
            if (!bl) {
                return;
            }
            entity.triggerItemPickedUpByEntityCriteria(item);
            int i = lv.getCount();
            ItemStack lv3 = lv2.addStack(lv);
            entity.sendPickup(item, i - lv3.getCount());
            if (lv3.isEmpty()) {
                item.discard();
            } else {
                lv.setCount(lv3.getCount());
            }
        }
    }

    default public void readInventory(ReadView view) {
        view.getOptionalTypedListView(INVENTORY_KEY, ItemStack.CODEC).ifPresent(list -> this.getInventory().readDataList((ReadView.TypedListReadView<ItemStack>)list));
    }

    default public void writeInventory(WriteView view) {
        this.getInventory().toDataList(view.getListAppender(INVENTORY_KEY, ItemStack.CODEC));
    }
}

