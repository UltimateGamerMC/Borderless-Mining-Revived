/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/ItemStream;filter(Ljava/util/function/Predicate;)Lnet/minecraft/loot/slot/ItemStream;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/slot/FilteredSlotSource;addSlotSourceField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.TransformSlotSource;
import net.minecraft.predicate.item.ItemPredicate;

public class FilteredSlotSource
extends TransformSlotSource {
    public static final MapCodec<FilteredSlotSource> CODEC = RecordCodecBuilder.mapCodec(instance -> FilteredSlotSource.addSlotSourceField(instance).and(((MapCodec)ItemPredicate.CODEC.fieldOf("item_filter")).forGetter(source -> source.itemFilter)).apply((Applicative<FilteredSlotSource, ?>)instance, FilteredSlotSource::new));
    private final ItemPredicate itemFilter;

    private FilteredSlotSource(SlotSource slotSource, ItemPredicate itemFilter) {
        super(slotSource);
        this.itemFilter = itemFilter;
    }

    public MapCodec<FilteredSlotSource> getCodec() {
        return CODEC;
    }

    @Override
    protected ItemStream transform(ItemStream stream) {
        return stream.filter(this.itemFilter);
    }
}

