/*
 * External method calls:
 *   Lnet/minecraft/loot/entry/LootPoolEntry;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;report(Lnet/minecraft/util/ErrorReporter$Error;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/loot/entry/EntryCombiner;expand(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/entry/CombinedEntry;combine(Ljava/util/List;)Lnet/minecraft/loot/entry/EntryCombiner;
 *   Lnet/minecraft/loot/entry/CombinedEntry;test(Lnet/minecraft/loot/context/LootContext;)Z
 *   Lnet/minecraft/loot/entry/CombinedEntry;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.EntryCombiner;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.util.ErrorReporter;

public abstract class CombinedEntry
extends LootPoolEntry {
    public static final ErrorReporter.Error EMPTY_CHILDREN_LIST_ERROR = new ErrorReporter.Error(){

        @Override
        public String getMessage() {
            return "Empty children list";
        }
    };
    protected final List<LootPoolEntry> children;
    private final EntryCombiner predicate;

    protected CombinedEntry(List<LootPoolEntry> terms, List<LootCondition> conditions) {
        super(conditions);
        this.children = terms;
        this.predicate = this.combine(terms);
    }

    @Override
    public void validate(LootTableReporter reporter) {
        super.validate(reporter);
        if (this.children.isEmpty()) {
            reporter.report(EMPTY_CHILDREN_LIST_ERROR);
        }
        for (int i = 0; i < this.children.size(); ++i) {
            this.children.get(i).validate(reporter.makeChild(new ErrorReporter.NamedListElementContext("children", i)));
        }
    }

    protected abstract EntryCombiner combine(List<? extends EntryCombiner> var1);

    @Override
    public final boolean expand(LootContext arg, Consumer<LootChoice> consumer) {
        if (!this.test(arg)) {
            return false;
        }
        return this.predicate.expand(arg, consumer);
    }

    public static <T extends CombinedEntry> MapCodec<T> createCodec(Factory<T> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(LootPoolEntryTypes.CODEC.listOf().optionalFieldOf("children", List.of()).forGetter(entry -> entry.children)).and(CombinedEntry.addConditionsField(instance).t1()).apply(instance, factory::create));
    }

    @FunctionalInterface
    public static interface Factory<T extends CombinedEntry> {
        public T create(List<LootPoolEntry> var1, List<LootCondition> var2);
    }
}

