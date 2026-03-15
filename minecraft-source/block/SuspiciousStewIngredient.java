/*
 * External method calls:
 *   Lnet/minecraft/registry/DefaultedRegistry;stream()Ljava/util/stream/Stream;
 *   Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;
 */
package net.minecraft.block;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import org.jspecify.annotations.Nullable;

public interface SuspiciousStewIngredient {
    public SuspiciousStewEffectsComponent getStewEffects();

    public static List<SuspiciousStewIngredient> getAll() {
        return Registries.ITEM.stream().map(SuspiciousStewIngredient::of).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static @Nullable SuspiciousStewIngredient of(ItemConvertible item) {
        BlockItem lv;
        ItemConvertible itemConvertible = item.asItem();
        if (itemConvertible instanceof BlockItem && (itemConvertible = (lv = (BlockItem)itemConvertible).getBlock()) instanceof SuspiciousStewIngredient) {
            SuspiciousStewIngredient lv2 = (SuspiciousStewIngredient)((Object)itemConvertible);
            return lv2;
        }
        Item item2 = item.asItem();
        if (item2 instanceof SuspiciousStewIngredient) {
            SuspiciousStewIngredient lv3 = (SuspiciousStewIngredient)((Object)item2);
            return lv3;
        }
        return null;
    }
}

