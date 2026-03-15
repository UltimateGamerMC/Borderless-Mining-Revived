/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/LootTableEntry;builder(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;weight(I)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;quality(I)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/entity/FishingHookPredicate;of(Z)Lnet/minecraft/predicate/entity/FishingHookPredicate;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;typeSpecific(Lnet/minecraft/predicate/entity/EntitySubPredicate;)Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;builder(Lnet/minecraft/loot/context/LootContext$EntityReference;Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;create(FF)Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;
 *   Lnet/minecraft/loot/function/SetDamageLootFunction;builder(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/function/SetPotionLootFunction;builder(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/function/SetCountLootFunction;builder(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/predicate/entity/LocationPredicate$Builder;create()Lnet/minecraft/predicate/entity/LocationPredicate$Builder;
 *   Lnet/minecraft/registry/entry/RegistryEntryList;of([Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/registry/entry/RegistryEntryList$Direct;
 *   Lnet/minecraft/predicate/entity/LocationPredicate$Builder;biome(Lnet/minecraft/registry/entry/RegistryEntryList;)Lnet/minecraft/predicate/entity/LocationPredicate$Builder;
 *   Lnet/minecraft/loot/condition/LocationCheckLootCondition;builder(Lnet/minecraft/predicate/entity/LocationPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/function/EnchantWithLevelsLootFunction;builder(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/EnchantWithLevelsLootFunction$Builder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/loottable/vanilla/VanillaFishingLootTableGenerator;createFishTableBuilder()Lnet/minecraft/loot/LootTable$Builder;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.block.Blocks;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.FishingHookPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.BiomeKeys;

public record VanillaFishingLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        RegistryEntryLookup lv = this.registries.getOrThrow(RegistryKeys.BIOME);
        lootTableBiConsumer.accept(LootTables.FISHING_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)((LeafEntry.Builder)LootTableEntry.builder(LootTables.FISHING_JUNK_GAMEPLAY).weight(10)).quality(-2)).with((LootPoolEntry.Builder<?>)((LootPoolEntry.Builder)((LeafEntry.Builder)LootTableEntry.builder(LootTables.FISHING_TREASURE_GAMEPLAY).weight(5)).quality(2)).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().typeSpecific(FishingHookPredicate.of(true))))).with((LootPoolEntry.Builder<?>)((LeafEntry.Builder)LootTableEntry.builder(LootTables.FISHING_FISH_GAMEPLAY).weight(85)).quality(-1))));
        lootTableBiConsumer.accept(LootTables.FISHING_FISH_GAMEPLAY, VanillaFishingLootTableGenerator.createFishTableBuilder());
        lootTableBiConsumer.accept(LootTables.FISHING_JUNK_GAMEPLAY, LootTable.builder().pool(LootPool.builder().with((LootPoolEntry.Builder<?>)ItemEntry.builder(Blocks.LILY_PAD).weight(17)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.LEATHER_BOOTS).weight(10)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.9f))))).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.LEATHER).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BONE).weight(10)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.POTION).weight(10)).apply(SetPotionLootFunction.builder(Potions.WATER)))).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.STRING).weight(5)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.FISHING_ROD).weight(2)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.9f))))).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BOWL).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.STICK).weight(5)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.INK_SAC).weight(1)).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(10.0f))))).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Blocks.TRIPWIRE_HOOK).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.ROTTEN_FLESH).weight(10)).with((LootPoolEntry.Builder<?>)((LeafEntry.Builder)ItemEntry.builder(Blocks.BAMBOO).conditionally(LocationCheckLootCondition.builder(LocationPredicate.Builder.create().biome(RegistryEntryList.of(lv.getOrThrow(BiomeKeys.JUNGLE), lv.getOrThrow(BiomeKeys.SPARSE_JUNGLE), lv.getOrThrow(BiomeKeys.BAMBOO_JUNGLE)))))).weight(10))));
        lootTableBiConsumer.accept(LootTables.FISHING_TREASURE_GAMEPLAY, LootTable.builder().pool(LootPool.builder().with(ItemEntry.builder(Items.NAME_TAG)).with(ItemEntry.builder(Items.SADDLE)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.BOW).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f)))).apply(EnchantWithLevelsLootFunction.builder(this.registries, ConstantLootNumberProvider.create(30.0f))))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.FISHING_ROD).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f)))).apply(EnchantWithLevelsLootFunction.builder(this.registries, ConstantLootNumberProvider.create(30.0f))))).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.BOOK).apply(EnchantWithLevelsLootFunction.builder(this.registries, ConstantLootNumberProvider.create(30.0f))))).with(ItemEntry.builder(Items.NAUTILUS_SHELL))));
    }

    public static LootTable.Builder createFishTableBuilder() {
        return LootTable.builder().pool(LootPool.builder().with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.COD).weight(60)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.SALMON).weight(25)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.TROPICAL_FISH).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.PUFFERFISH).weight(13)));
    }
}

