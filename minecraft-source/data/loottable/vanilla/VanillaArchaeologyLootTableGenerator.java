/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;weight(I)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/function/SetStewEffectLootFunction;builder()Lnet/minecraft/loot/function/SetStewEffectLootFunction$Builder;
 *   Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;create(FF)Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;
 *   Lnet/minecraft/loot/function/SetStewEffectLootFunction$Builder;withEffect(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/SetStewEffectLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetStewEffectLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public record VanillaArchaeologyLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(LootTables.DESERT_WELL_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.ARMS_UP_POTTERY_SHERD).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BREWER_POTTERY_SHERD).weight(2)).with(ItemEntry.builder(Items.BRICK)).with(ItemEntry.builder(Items.EMERALD)).with(ItemEntry.builder(Items.STICK)).with((LootPoolEntry.Builder<?>)((Object)ItemEntry.builder(Items.SUSPICIOUS_STEW).apply(SetStewEffectLootFunction.builder().withEffect(StatusEffects.NIGHT_VISION, UniformLootNumberProvider.create(7.0f, 10.0f)).withEffect(StatusEffects.JUMP_BOOST, UniformLootNumberProvider.create(7.0f, 10.0f)).withEffect(StatusEffects.WEAKNESS, UniformLootNumberProvider.create(6.0f, 8.0f)).withEffect(StatusEffects.BLINDNESS, UniformLootNumberProvider.create(5.0f, 7.0f)).withEffect(StatusEffects.POISON, UniformLootNumberProvider.create(10.0f, 20.0f)).withEffect(StatusEffects.SATURATION, UniformLootNumberProvider.create(7.0f, 10.0f)))))));
        lootTableBiConsumer.accept(LootTables.DESERT_PYRAMID_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.ARCHER_POTTERY_SHERD)).with(ItemEntry.builder(Items.MINER_POTTERY_SHERD)).with(ItemEntry.builder(Items.PRIZE_POTTERY_SHERD)).with(ItemEntry.builder(Items.SKULL_POTTERY_SHERD)).with(ItemEntry.builder(Items.DIAMOND)).with(ItemEntry.builder(Items.TNT)).with(ItemEntry.builder(Items.GUNPOWDER)).with(ItemEntry.builder(Items.EMERALD))));
        lootTableBiConsumer.accept(LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WHEAT).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WOODEN_HOE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.CLAY).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BRICK).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.YELLOW_DYE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BLUE_DYE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.LIGHT_BLUE_DYE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WHITE_DYE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.ORANGE_DYE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.RED_CANDLE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.GREEN_CANDLE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.PURPLE_CANDLE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.BROWN_CANDLE).weight(2)).with(ItemEntry.builder(Items.MAGENTA_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.PINK_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.BLUE_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.LIGHT_BLUE_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.RED_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.YELLOW_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.PURPLE_STAINED_GLASS_PANE)).with(ItemEntry.builder(Items.SPRUCE_HANGING_SIGN)).with(ItemEntry.builder(Items.OAK_HANGING_SIGN)).with(ItemEntry.builder(Items.GOLD_NUGGET)).with(ItemEntry.builder(Items.COAL)).with(ItemEntry.builder(Items.WHEAT_SEEDS)).with(ItemEntry.builder(Items.BEETROOT_SEEDS)).with(ItemEntry.builder(Items.DEAD_BUSH)).with(ItemEntry.builder(Items.FLOWER_POT)).with(ItemEntry.builder(Items.STRING)).with(ItemEntry.builder(Items.LEAD))));
        lootTableBiConsumer.accept(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.BURN_POTTERY_SHERD)).with(ItemEntry.builder(Items.DANGER_POTTERY_SHERD)).with(ItemEntry.builder(Items.FRIEND_POTTERY_SHERD)).with(ItemEntry.builder(Items.HEART_POTTERY_SHERD)).with(ItemEntry.builder(Items.HEARTBREAK_POTTERY_SHERD)).with(ItemEntry.builder(Items.HOWL_POTTERY_SHERD)).with(ItemEntry.builder(Items.SHEAF_POTTERY_SHERD)).with(ItemEntry.builder(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)).with(ItemEntry.builder(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)).with(ItemEntry.builder(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)).with(ItemEntry.builder(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)).with(ItemEntry.builder(Items.MUSIC_DISC_RELIC))));
        lootTableBiConsumer.accept(LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.ANGLER_POTTERY_SHERD)).with(ItemEntry.builder(Items.SHELTER_POTTERY_SHERD)).with(ItemEntry.builder(Items.SNORT_POTTERY_SHERD)).with(ItemEntry.builder(Items.SNIFFER_EGG)).with(ItemEntry.builder(Items.IRON_AXE)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WHEAT).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WOODEN_HOE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.COAL).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.GOLD_NUGGET).weight(2))));
        lootTableBiConsumer.accept(LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.BLADE_POTTERY_SHERD)).with(ItemEntry.builder(Items.EXPLORER_POTTERY_SHERD)).with(ItemEntry.builder(Items.MOURNER_POTTERY_SHERD)).with(ItemEntry.builder(Items.PLENTY_POTTERY_SHERD)).with(ItemEntry.builder(Items.IRON_AXE)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WHEAT).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.WOODEN_HOE).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.COAL).weight(2)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.GOLD_NUGGET).weight(2))));
    }
}

