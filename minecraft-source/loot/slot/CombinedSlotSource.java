/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/SlotSources;concat(Ljava/util/Collection;)Ljava/util/function/Function;
 *   Lnet/minecraft/loot/slot/SlotSource;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Function;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.SlotSources;
import net.minecraft.util.ErrorReporter;

public abstract class CombinedSlotSource
implements SlotSource {
    protected final List<SlotSource> terms;
    private final Function<LootContext, ItemStream> source;

    protected CombinedSlotSource(List<SlotSource> terms) {
        this.terms = terms;
        this.source = SlotSources.concat(terms);
    }

    protected static <T extends CombinedSlotSource> MapCodec<T> createCodec(Function<List<SlotSource>, T> termsToSource) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)SlotSources.CODEC.listOf().fieldOf("terms")).forGetter(source -> source.terms)).apply((Applicative<CombinedSlotSource, ?>)instance, termsToSource));
    }

    protected static <T extends CombinedSlotSource> Codec<T> createInlineCodec(Function<List<SlotSource>, T> termsToSource) {
        return SlotSources.CODEC.listOf().xmap(termsToSource, source -> source.terms);
    }

    public abstract MapCodec<? extends CombinedSlotSource> getCodec();

    @Override
    public ItemStream stream(LootContext context) {
        return this.source.apply(context);
    }

    @Override
    public void validate(LootTableReporter reporter) {
        SlotSource.super.validate(reporter);
        for (int i = 0; i < this.terms.size(); ++i) {
            this.terms.get(i).validate(reporter.makeChild(new ErrorReporter.NamedListElementContext("terms", i)));
        }
    }
}

