/*
 * Internal private/static methods:
 *   Lnet/minecraft/loot/entry/EmptyEntry;builder(Lnet/minecraft/loot/entry/LeafEntry$Factory;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/EmptyEntry;addLeafFields(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P4;
 */
package net.minecraft.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.loot.function.LootFunction;

public class EmptyEntry
extends LeafEntry {
    public static final MapCodec<EmptyEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> EmptyEntry.addLeafFields(instance).apply(instance, EmptyEntry::new));

    private EmptyEntry(int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntryTypes.EMPTY;
    }

    @Override
    public void generateLoot(Consumer<ItemStack> lootConsumer, LootContext context) {
    }

    public static LeafEntry.Builder<?> builder() {
        return EmptyEntry.builder(EmptyEntry::new);
    }
}

