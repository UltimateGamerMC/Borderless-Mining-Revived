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
 *   Lnet/minecraft/data/loottable/EntityLootTableGenerator;createForSheep(Ljava/util/Map;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/LootTableEntry;builder(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;create()Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentMapPredicate;of(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/predicate/component/ComponentMapPredicate;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;exact(Lnet/minecraft/predicate/component/ComponentMapPredicate;)Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;build()Lnet/minecraft/predicate/component/ComponentsPredicate;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;components(Lnet/minecraft/predicate/component/ComponentsPredicate;)Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;builder(Lnet/minecraft/loot/context/LootContext$EntityReference;Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;
 *   Lnet/minecraft/loot/entry/AlternativeEntry;builder([Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/entry/AlternativeEntry$Builder;
 *   Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;create(FF)Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.loottable.EntityLootTableGenerator;
import net.minecraft.data.loottable.LootTableData;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public record VanillaShearingLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(LootTables.BOGGED_SHEARING, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(2.0f)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.BROWN_MUSHROOM).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.RED_MUSHROOM).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))))));
        LootTableData.WOOL_FROM_DYE_COLOR.forEach((color, wool) -> lootTableBiConsumer.accept(LootTables.SHEEP_SHEARING_FROM_DYE_COLOR.get(color), LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f, 3.0f)).with(ItemEntry.builder(wool)))));
        lootTableBiConsumer.accept(LootTables.SHEEP_SHEARING, LootTable.builder().pool(EntityLootTableGenerator.createForSheep(LootTables.SHEEP_SHEARING_FROM_DYE_COLOR)));
        lootTableBiConsumer.accept(LootTables.MOOSHROOM_SHEARING, LootTable.builder().pool(LootPool.builder().with(AlternativeEntry.builder(new LootPoolEntry.Builder[]{LootTableEntry.builder(LootTables.MOOSHROOM_RED_SHEARING).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().components(ComponentsPredicate.Builder.create().exact(ComponentMapPredicate.of(DataComponentTypes.MOOSHROOM_VARIANT, MooshroomEntity.Variant.RED)).build()))), LootTableEntry.builder(LootTables.MOOSHROOM_BROWN_SHEARING).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().components(ComponentsPredicate.Builder.create().exact(ComponentMapPredicate.of(DataComponentTypes.MOOSHROOM_VARIANT, MooshroomEntity.Variant.BROWN)).build())))}))));
        lootTableBiConsumer.accept(LootTables.MOOSHROOM_RED_SHEARING, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(5.0f)).with(ItemEntry.builder(Items.RED_MUSHROOM))));
        lootTableBiConsumer.accept(LootTables.MOOSHROOM_BROWN_SHEARING, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(5.0f)).with(ItemEntry.builder(Items.BROWN_MUSHROOM))));
        lootTableBiConsumer.accept(LootTables.SNOW_GOLEM_SHEARING, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.CARVED_PUMPKIN))));
    }
}

