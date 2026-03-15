/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/function/SetCountLootFunction;builder(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/condition/BlockStatePropertyLootCondition;builder(Lnet/minecraft/block/Block;)Lnet/minecraft/loot/condition/BlockStatePropertyLootCondition$Builder;
 *   Lnet/minecraft/predicate/StatePredicate$Builder;create()Lnet/minecraft/predicate/StatePredicate$Builder;
 *   Lnet/minecraft/predicate/StatePredicate$Builder;exactMatch(Lnet/minecraft/state/property/Property;I)Lnet/minecraft/predicate/StatePredicate$Builder;
 *   Lnet/minecraft/loot/condition/BlockStatePropertyLootCondition$Builder;properties(Lnet/minecraft/predicate/StatePredicate$Builder;)Lnet/minecraft/loot/condition/BlockStatePropertyLootCondition$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;
 *   Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;create(FF)Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public record VanillaHarvestLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(LootTables.BEEHIVE_HARVEST, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.HONEYCOMB).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(3.0f)))))));
        lootTableBiConsumer.accept(LootTables.CAVE_VINE_HARVEST, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.GLOW_BERRIES))));
        lootTableBiConsumer.accept(LootTables.SWEET_BERRY_BUSH_HARVEST, LootTable.builder().pool(LootPool.builder().with((LootPoolEntry.Builder<?>)((LootPoolEntry.Builder)((Object)ItemEntry.builder(Items.SWEET_BERRIES).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))).conditionally(BlockStatePropertyLootCondition.builder(Blocks.SWEET_BERRY_BUSH).properties(StatePredicate.Builder.create().exactMatch(SweetBerryBushBlock.AGE, 3))))).pool(LootPool.builder().with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.SWEET_BERRIES).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))))));
        lootTableBiConsumer.accept(LootTables.PUMPKIN_CARVE, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.PUMPKIN_SEEDS).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0f)))))));
    }
}

