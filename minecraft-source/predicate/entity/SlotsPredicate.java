/*
 * External method calls:
 *   Lnet/minecraft/predicate/item/ItemPredicate;test(Lnet/minecraft/item/ItemStack;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/predicate/entity/SlotsPredicate;matches(Lnet/minecraft/inventory/StackReferenceGetter;Lnet/minecraft/predicate/item/ItemPredicate;Lit/unimi/dsi/fastutil/ints/IntList;)Z
 */
package net.minecraft.predicate.entity;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Map;
import net.minecraft.inventory.SlotRange;
import net.minecraft.inventory.SlotRanges;
import net.minecraft.inventory.StackReference;
import net.minecraft.inventory.StackReferenceGetter;
import net.minecraft.predicate.item.ItemPredicate;

public record SlotsPredicate(Map<SlotRange, ItemPredicate> slots) {
    public static final Codec<SlotsPredicate> CODEC = Codec.unboundedMap(SlotRanges.CODEC, ItemPredicate.CODEC).xmap(SlotsPredicate::new, SlotsPredicate::slots);

    public boolean matches(StackReferenceGetter arg) {
        for (Map.Entry<SlotRange, ItemPredicate> entry : this.slots.entrySet()) {
            if (SlotsPredicate.matches(arg, entry.getValue(), entry.getKey().getSlotIds())) continue;
            return false;
        }
        return true;
    }

    private static boolean matches(StackReferenceGetter arg, ItemPredicate itemPredicate, IntList slotIds) {
        for (int i = 0; i < slotIds.size(); ++i) {
            int j = slotIds.getInt(i);
            StackReference lv = arg.getStackReference(j);
            if (lv == null || !itemPredicate.test(lv.get())) continue;
            return true;
        }
        return false;
    }
}

