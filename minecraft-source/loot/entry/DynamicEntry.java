/*
 * External method calls:
 *   Lnet/minecraft/loot/context/LootContext;drop(Lnet/minecraft/util/Identifier;Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/entry/DynamicEntry;builder(Lnet/minecraft/loot/entry/LeafEntry$Factory;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/DynamicEntry;addLeafFields(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P4;
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
import net.minecraft.util.Identifier;

public class DynamicEntry
extends LeafEntry {
    public static final MapCodec<DynamicEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("name")).forGetter(entry -> entry.name)).and(DynamicEntry.addLeafFields(instance)).apply(instance, DynamicEntry::new));
    private final Identifier name;

    private DynamicEntry(Identifier name, int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        super(weight, quality, conditions, functions);
        this.name = name;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntryTypes.DYNAMIC;
    }

    @Override
    public void generateLoot(Consumer<ItemStack> lootConsumer, LootContext context) {
        context.drop(this.name, lootConsumer);
    }

    public static LeafEntry.Builder<?> builder(Identifier name) {
        return DynamicEntry.builder((int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) -> new DynamicEntry(name, weight, quality, conditions, functions));
    }
}

