/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/SlotSource;stream(Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/loot/slot/ItemStream;
 *   Lnet/minecraft/loot/slot/SlotSource;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/slot/TransformSlotSource;transform(Lnet/minecraft/loot/slot/ItemStream;)Lnet/minecraft/loot/slot/ItemStream;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.Products;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.SlotSources;
import net.minecraft.util.ErrorReporter;

public abstract class TransformSlotSource
implements SlotSource {
    protected final SlotSource slotSource;

    protected TransformSlotSource(SlotSource slotSource) {
        this.slotSource = slotSource;
    }

    public abstract MapCodec<? extends TransformSlotSource> getCodec();

    protected static <T extends TransformSlotSource> Products.P1<RecordCodecBuilder.Mu<T>, SlotSource> addSlotSourceField(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(((MapCodec)SlotSources.CODEC.fieldOf("slot_source")).forGetter(source -> source.slotSource));
    }

    protected abstract ItemStream transform(ItemStream var1);

    @Override
    public final ItemStream stream(LootContext context) {
        return this.transform(this.slotSource.stream(context));
    }

    @Override
    public void validate(LootTableReporter reporter) {
        SlotSource.super.validate(reporter);
        this.slotSource.validate(reporter.makeChild(new ErrorReporter.MapElementContext("slot_source")));
    }
}

