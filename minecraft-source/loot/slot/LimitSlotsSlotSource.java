/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/ItemStream;limit(I)Lnet/minecraft/loot/slot/ItemStream;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/slot/LimitSlotsSlotSource;addSlotSourceField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.TransformSlotSource;
import net.minecraft.util.dynamic.Codecs;

public class LimitSlotsSlotSource
extends TransformSlotSource {
    public static final MapCodec<LimitSlotsSlotSource> CODEC = RecordCodecBuilder.mapCodec(instance -> LimitSlotsSlotSource.addSlotSourceField(instance).and(((MapCodec)Codecs.POSITIVE_INT.fieldOf("limit")).forGetter(source -> source.limit)).apply((Applicative<LimitSlotsSlotSource, ?>)instance, LimitSlotsSlotSource::new));
    private final int limit;

    private LimitSlotsSlotSource(SlotSource slotSource, int limit) {
        super(slotSource);
        this.limit = limit;
    }

    public MapCodec<LimitSlotsSlotSource> getCodec() {
        return CODEC;
    }

    @Override
    protected ItemStream transform(ItemStream stream) {
        return stream.limit(this.limit);
    }
}

