/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/entity/EntityTypePredicate;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/predicate/entity/EntityTypePredicate;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;type(Lnet/minecraft/predicate/entity/EntityTypePredicate;)Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;builder(Lnet/minecraft/loot/context/LootContext$EntityReference;Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/entry/LootTableEntry;builder(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;
 *   Lnet/minecraft/loot/entry/AlternativeEntry;builder([Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/entry/AlternativeEntry$Builder;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public record VanillaChargedCreeperLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    private static final List<Table> TABLES = List.of(new Table(LootTables.PIGLIN_CHARGED_CREEPER, EntityType.PIGLIN, Items.PIGLIN_HEAD), new Table(LootTables.CREEPER_CHARGED_CREEPER, EntityType.CREEPER, Items.CREEPER_HEAD), new Table(LootTables.SKELETON_CHARGED_CREEPER, EntityType.SKELETON, Items.SKELETON_SKULL), new Table(LootTables.WITHER_SKELETON_CHARGED_CREEPER, EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL), new Table(LootTables.ZOMBIE_CHARGED_CREEPER, EntityType.ZOMBIE, Items.ZOMBIE_HEAD));

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        RegistryEntryLookup lv = this.registries.getOrThrow(RegistryKeys.ENTITY_TYPE);
        ArrayList<LootConditionConsumingBuilder> list = new ArrayList<LootConditionConsumingBuilder>(TABLES.size());
        for (Table lv2 : TABLES) {
            lootTableBiConsumer.accept(lv2.lootTable, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(lv2.item))));
            LootCondition.Builder lv3 = EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().type(EntityTypePredicate.create(lv, lv2.entityType)));
            list.add(LootTableEntry.builder(lv2.lootTable).conditionally(lv3));
        }
        lootTableBiConsumer.accept(LootTables.ROOT_CHARGED_CREEPER, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(AlternativeEntry.builder((LootPoolEntry.Builder[])list.toArray(LootPoolEntry.Builder[]::new)))));
    }

    record Table(RegistryKey<LootTable> lootTable, EntityType<?> entityType, Item item) {
    }
}

