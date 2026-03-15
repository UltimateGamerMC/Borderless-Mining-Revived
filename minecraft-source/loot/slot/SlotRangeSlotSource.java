/*
 * External method calls:
 *   Lnet/minecraft/loot/context/LootEntityValueSource;contextParam()Lnet/minecraft/util/context/ContextParameter;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.inventory.SlotRange;
import net.minecraft.inventory.SlotRanges;
import net.minecraft.inventory.StackReferenceGetter;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootEntityValueSource;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.util.context.ContextParameter;

public class SlotRangeSlotSource
implements SlotSource {
    public static final MapCodec<SlotRangeSlotSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LootEntityValueSource.ENTITY_OR_BLOCK_ENTITY_CODEC.fieldOf("source")).forGetter(arg -> arg.field_64159), ((MapCodec)SlotRanges.CODEC.fieldOf("slots")).forGetter(arg -> arg.field_64160)).apply((Applicative<SlotRangeSlotSource, ?>)instance, SlotRangeSlotSource::new));
    private final LootEntityValueSource<Object> field_64159;
    private final SlotRange field_64160;

    private SlotRangeSlotSource(LootEntityValueSource<Object> arg, SlotRange arg2) {
        this.field_64159 = arg;
        this.field_64160 = arg2;
    }

    public MapCodec<SlotRangeSlotSource> getCodec() {
        return CODEC;
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Set.of(this.field_64159.contextParam());
    }

    @Override
    public final ItemStream stream(LootContext context) {
        Object object = this.field_64159.get(context);
        if (object instanceof StackReferenceGetter) {
            StackReferenceGetter lv = (StackReferenceGetter)object;
            return lv.getStackReferences(this.field_64160.getSlotIds());
        }
        return ItemStream.EMPTY;
    }
}

