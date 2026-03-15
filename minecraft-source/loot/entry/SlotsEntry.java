/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/SlotSource;stream(Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/loot/slot/ItemStream;
 *   Lnet/minecraft/loot/slot/ItemStream;itemCopies()Ljava/util/stream/Stream;
 *   Lnet/minecraft/loot/entry/LeafEntry;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/loot/slot/SlotSource;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/entry/SlotsEntry;addLeafFields(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P4;
 */
package net.minecraft.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.SlotSources;
import net.minecraft.util.ErrorReporter;

public class SlotsEntry
extends LeafEntry {
    public static final MapCodec<SlotsEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)SlotSources.CODEC.fieldOf("slot_source")).forGetter(entry -> entry.slotSource)).and(SlotsEntry.addLeafFields(instance)).apply(instance, SlotsEntry::new));
    private final SlotSource slotSource;

    private SlotsEntry(SlotSource slotSource, int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        super(weight, quality, conditions, functions);
        this.slotSource = slotSource;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntryTypes.SLOTS;
    }

    @Override
    public void generateLoot(Consumer<ItemStack> lootConsumer, LootContext context) {
        this.slotSource.stream(context).itemCopies().filter(stack -> !stack.isEmpty()).forEach(lootConsumer);
    }

    @Override
    public void validate(LootTableReporter reporter) {
        super.validate(reporter);
        this.slotSource.validate(reporter.makeChild(new ErrorReporter.MapElementContext("slot_source")));
    }
}

