/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;build()Lnet/minecraft/loot/LootTable;
 *   Lnet/minecraft/loot/entry/LootTableEntry;builder(Lnet/minecraft/loot/LootTable;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;weight(I)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/entry/LootTableEntry;builder(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/function/SetEnchantmentsLootFunction$Builder;enchantment(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/SetEnchantmentsLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/condition/RandomChanceLootCondition;builder(F)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/function/SetComponentsLootFunction;builder(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/loottable/vanilla/VanillaEquipmentLootTableGenerator;createEquipmentTableBuilder(Lnet/minecraft/item/Item;Lnet/minecraft/item/Item;Lnet/minecraft/item/equipment/trim/ArmorTrim;Lnet/minecraft/registry/RegistryWrapper$Impl;)Lnet/minecraft/loot/LootTable$Builder;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public record VanillaEquipmentLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        RegistryEntryLookup lv = this.registries.getOrThrow(RegistryKeys.TRIM_PATTERN);
        RegistryEntryLookup lv2 = this.registries.getOrThrow(RegistryKeys.TRIM_MATERIAL);
        RegistryEntryLookup lv3 = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        ArmorTrim lv4 = new ArmorTrim(lv2.getOrThrow(ArmorTrimMaterials.COPPER), lv.getOrThrow(ArmorTrimPatterns.FLOW));
        ArmorTrim lv5 = new ArmorTrim(lv2.getOrThrow(ArmorTrimMaterials.COPPER), lv.getOrThrow(ArmorTrimPatterns.BOLT));
        lootTableBiConsumer.accept(LootTables.TRIAL_CHAMBER_EQUIPMENT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)LootTableEntry.builder(VanillaEquipmentLootTableGenerator.createEquipmentTableBuilder(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, lv5, (RegistryWrapper.Impl<Enchantment>)lv3).build()).weight(4)).with((LootPoolEntry.Builder<?>)LootTableEntry.builder(VanillaEquipmentLootTableGenerator.createEquipmentTableBuilder(Items.IRON_HELMET, Items.IRON_CHESTPLATE, lv4, (RegistryWrapper.Impl<Enchantment>)lv3).build()).weight(2)).with((LootPoolEntry.Builder<?>)LootTableEntry.builder(VanillaEquipmentLootTableGenerator.createEquipmentTableBuilder(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, lv4, (RegistryWrapper.Impl<Enchantment>)lv3).build()).weight(1))));
        lootTableBiConsumer.accept(LootTables.TRIAL_CHAMBER_MELEE_EQUIPMENT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(LootTableEntry.builder(LootTables.TRIAL_CHAMBER_EQUIPMENT))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.IRON_SWORD).weight(4)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.IRON_SWORD).apply(new SetEnchantmentsLootFunction.Builder().enchantment(lv3.getOrThrow(Enchantments.SHARPNESS), ConstantLootNumberProvider.create(1.0f))))).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.IRON_SWORD).apply(new SetEnchantmentsLootFunction.Builder().enchantment(lv3.getOrThrow(Enchantments.KNOCKBACK), ConstantLootNumberProvider.create(1.0f))))).with(ItemEntry.builder(Items.DIAMOND_SWORD))));
        lootTableBiConsumer.accept(LootTables.TRIAL_CHAMBER_RANGED_EQUIPMENT, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(LootTableEntry.builder(LootTables.TRIAL_CHAMBER_EQUIPMENT))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BOW).weight(2)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.BOW).apply(new SetEnchantmentsLootFunction.Builder().enchantment(lv3.getOrThrow(Enchantments.POWER), ConstantLootNumberProvider.create(1.0f))))).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.BOW).apply(new SetEnchantmentsLootFunction.Builder().enchantment(lv3.getOrThrow(Enchantments.PUNCH), ConstantLootNumberProvider.create(1.0f)))))));
    }

    public static LootTable.Builder createEquipmentTableBuilder(Item helmet, Item chestplate, ArmorTrim trim, RegistryWrapper.Impl<Enchantment> enchantmentRegistryWrapper) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).conditionally(RandomChanceLootCondition.builder(0.5f)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(helmet).apply(SetComponentsLootFunction.builder(DataComponentTypes.TRIM, trim))).apply(new SetEnchantmentsLootFunction.Builder().enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.PROTECTION), ConstantLootNumberProvider.create(4.0f)).enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantLootNumberProvider.create(4.0f)).enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantLootNumberProvider.create(4.0f)))))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).conditionally(RandomChanceLootCondition.builder(0.5f)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(chestplate).apply(SetComponentsLootFunction.builder(DataComponentTypes.TRIM, trim))).apply(new SetEnchantmentsLootFunction.Builder().enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.PROTECTION), ConstantLootNumberProvider.create(4.0f)).enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantLootNumberProvider.create(4.0f)).enchantment(enchantmentRegistryWrapper.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantLootNumberProvider.create(4.0f))))));
    }
}

