/*
 * External method calls:
 *   Lnet/minecraft/inventory/StackWithSlot;stack()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/block/entity/EnderChestBlockEntity;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/inventory/SimpleInventory;onOpen(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/block/entity/EnderChestBlockEntity;onClose(Lnet/minecraft/entity/ContainerUser;)V
 *   Lnet/minecraft/inventory/SimpleInventory;onClose(Lnet/minecraft/entity/ContainerUser;)V
 */
package net.minecraft.inventory;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.ContainerUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jspecify.annotations.Nullable;

public class EnderChestInventory
extends SimpleInventory {
    private @Nullable EnderChestBlockEntity activeBlockEntity;

    public EnderChestInventory() {
        super(27);
    }

    public void setActiveBlockEntity(EnderChestBlockEntity blockEntity) {
        this.activeBlockEntity = blockEntity;
    }

    public boolean isActiveBlockEntity(EnderChestBlockEntity blockEntity) {
        return this.activeBlockEntity == blockEntity;
    }

    public void readData(ReadView.TypedListReadView<StackWithSlot> list) {
        for (int i = 0; i < this.size(); ++i) {
            this.setStack(i, ItemStack.EMPTY);
        }
        for (StackWithSlot lv : list) {
            if (!lv.isValidSlot(this.size())) continue;
            this.setStack(lv.slot(), lv.stack());
        }
    }

    public void writeData(WriteView.ListAppender<StackWithSlot> list) {
        for (int i = 0; i < this.size(); ++i) {
            ItemStack lv = this.getStack(i);
            if (lv.isEmpty()) continue;
            list.add(new StackWithSlot(i, lv));
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.activeBlockEntity != null && !this.activeBlockEntity.canPlayerUse(player)) {
            return false;
        }
        return super.canPlayerUse(player);
    }

    @Override
    public void onOpen(ContainerUser user) {
        if (this.activeBlockEntity != null) {
            this.activeBlockEntity.onOpen(user);
        }
        super.onOpen(user);
    }

    @Override
    public void onClose(ContainerUser user) {
        if (this.activeBlockEntity != null) {
            this.activeBlockEntity.onClose(user);
        }
        super.onClose(user);
        this.activeBlockEntity = null;
    }
}

