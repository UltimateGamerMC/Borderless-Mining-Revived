/*
 * External method calls:
 *   Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/LivingEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/dispenser/EquippableDispenserBehavior;dispense(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Z
 */
package net.minecraft.block.dispenser;

import java.util.List;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class EquippableDispenserBehavior
extends ItemDispenserBehavior {
    public static final EquippableDispenserBehavior INSTANCE = new EquippableDispenserBehavior();

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        return EquippableDispenserBehavior.dispense(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
    }

    public static boolean dispense(BlockPointer pointer, ItemStack stack) {
        BlockPos lv = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        List<LivingEntity> list = pointer.world().getEntitiesByClass(LivingEntity.class, new Box(lv), entity -> entity.canEquipFromDispenser(stack));
        if (list.isEmpty()) {
            return false;
        }
        LivingEntity lv2 = list.getFirst();
        EquipmentSlot lv3 = lv2.getPreferredEquipmentSlot(stack);
        ItemStack lv4 = stack.split(1);
        lv2.equipStack(lv3, lv4);
        if (lv2 instanceof MobEntity) {
            MobEntity lv5 = (MobEntity)lv2;
            lv5.setDropGuaranteed(lv3);
            lv5.setPersistent();
        }
        return true;
    }
}

