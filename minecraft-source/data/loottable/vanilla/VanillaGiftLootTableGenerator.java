/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTable;builder()Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootPool;builder()Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/LootPool$Builder;rolls(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/entry/ItemEntry;builder(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;weight(I)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/LootPool$Builder;with(Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/LootPool$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;pool(Lnet/minecraft/loot/LootPool$Builder;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;create(FF)Lnet/minecraft/loot/provider/number/UniformLootNumberProvider;
 *   Lnet/minecraft/loot/function/SetCountLootFunction;builder(Lnet/minecraft/loot/provider/number/LootNumberProvider;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/loot/function/SetPotionLootFunction;builder(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/entry/EmptyEntry;builder()Lnet/minecraft/loot/entry/LeafEntry$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;create()Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentMapPredicate;of(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/predicate/component/ComponentMapPredicate;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;exact(Lnet/minecraft/predicate/component/ComponentMapPredicate;)Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;
 *   Lnet/minecraft/predicate/component/ComponentsPredicate$Builder;build()Lnet/minecraft/predicate/component/ComponentsPredicate;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;components(Lnet/minecraft/predicate/component/ComponentsPredicate;)Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;builder(Lnet/minecraft/loot/context/LootContext$EntityReference;Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/entry/LeafEntry$Builder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/entry/LootPoolEntry$Builder;
 *   Lnet/minecraft/loot/entry/AlternativeEntry;builder([Lnet/minecraft/loot/entry/LootPoolEntry$Builder;)Lnet/minecraft/loot/entry/AlternativeEntry$Builder;
 */
package net.minecraft.data.loottable.vanilla;

import java.util.function.BiConsumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.passive.ChickenVariants;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.LazyRegistryEntryReference;

public record VanillaGiftLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator
{
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        RegistryEntryLookup lv = this.registries.getOrThrow(RegistryKeys.CHICKEN_VARIANT);
        lootTableBiConsumer.accept(LootTables.CAT_MORNING_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.RABBIT_HIDE).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.RABBIT_FOOT).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.CHICKEN).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.FEATHER).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.ROTTEN_FLESH).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.STRING).weight(10)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.PHANTOM_MEMBRANE).weight(2))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_ARMORER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.CHAINMAIL_HELMET)).with(ItemEntry.builder(Items.CHAINMAIL_CHESTPLATE)).with(ItemEntry.builder(Items.CHAINMAIL_LEGGINGS)).with(ItemEntry.builder(Items.CHAINMAIL_BOOTS))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_BUTCHER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.COOKED_RABBIT)).with(ItemEntry.builder(Items.COOKED_CHICKEN)).with(ItemEntry.builder(Items.COOKED_PORKCHOP)).with(ItemEntry.builder(Items.COOKED_BEEF)).with(ItemEntry.builder(Items.COOKED_MUTTON))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_CARTOGRAPHER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.MAP)).with(ItemEntry.builder(Items.PAPER))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_CLERIC_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.REDSTONE)).with(ItemEntry.builder(Items.LAPIS_LAZULI))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_FARMER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.BREAD)).with(ItemEntry.builder(Items.PUMPKIN_PIE)).with(ItemEntry.builder(Items.COOKIE))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_FISHERMAN_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.COD)).with(ItemEntry.builder(Items.SALMON))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_FLETCHER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.ARROW).weight(26)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.SWIFTNESS)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.SLOWNESS)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.STRENGTH)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.HEALING)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.HARMING)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.LEAPING)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.REGENERATION)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.FIRE_RESISTANCE)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.WATER_BREATHING)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.INVISIBILITY)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.NIGHT_VISION)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.WEAKNESS)))).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.TIPPED_ARROW).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))).apply(SetPotionLootFunction.builder(Potions.POISON))))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_LEATHERWORKER_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.LEATHER))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.BOOK))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_MASON_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.CLAY))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_SHEPHERD_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.WHITE_WOOL)).with(ItemEntry.builder(Items.ORANGE_WOOL)).with(ItemEntry.builder(Items.MAGENTA_WOOL)).with(ItemEntry.builder(Items.LIGHT_BLUE_WOOL)).with(ItemEntry.builder(Items.YELLOW_WOOL)).with(ItemEntry.builder(Items.LIME_WOOL)).with(ItemEntry.builder(Items.PINK_WOOL)).with(ItemEntry.builder(Items.GRAY_WOOL)).with(ItemEntry.builder(Items.LIGHT_GRAY_WOOL)).with(ItemEntry.builder(Items.CYAN_WOOL)).with(ItemEntry.builder(Items.PURPLE_WOOL)).with(ItemEntry.builder(Items.BLUE_WOOL)).with(ItemEntry.builder(Items.BROWN_WOOL)).with(ItemEntry.builder(Items.GREEN_WOOL)).with(ItemEntry.builder(Items.RED_WOOL)).with(ItemEntry.builder(Items.BLACK_WOOL))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.STONE_PICKAXE)).with(ItemEntry.builder(Items.STONE_AXE)).with(ItemEntry.builder(Items.STONE_HOE)).with(ItemEntry.builder(Items.STONE_SHOVEL))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.STONE_AXE)).with(ItemEntry.builder(Items.GOLDEN_AXE)).with(ItemEntry.builder(Items.IRON_AXE))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_UNEMPLOYED_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.WHEAT_SEEDS))));
        lootTableBiConsumer.accept(LootTables.HERO_OF_THE_VILLAGE_BABY_GIFT_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.POPPY))));
        lootTableBiConsumer.accept(LootTables.SNIFFER_DIGGING_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.TORCHFLOWER_SEEDS)).with(ItemEntry.builder(Items.PITCHER_POD))));
        lootTableBiConsumer.accept(LootTables.PANDA_SNEEZE_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with((LootPoolEntry.Builder<?>)ItemEntry.builder(Items.SLIME_BALL).weight(1)).with((LootPoolEntry.Builder<?>)EmptyEntry.builder().weight(699))));
        lootTableBiConsumer.accept(LootTables.CHICKEN_LAY_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(AlternativeEntry.builder(new LootPoolEntry.Builder[]{ItemEntry.builder(Items.EGG).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().components(ComponentsPredicate.Builder.create().exact(ComponentMapPredicate.of(DataComponentTypes.CHICKEN_VARIANT, new LazyRegistryEntryReference<ChickenVariant>(lv.getOrThrow(ChickenVariants.TEMPERATE)))).build()))), ItemEntry.builder(Items.BROWN_EGG).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().components(ComponentsPredicate.Builder.create().exact(ComponentMapPredicate.of(DataComponentTypes.CHICKEN_VARIANT, new LazyRegistryEntryReference<ChickenVariant>(lv.getOrThrow(ChickenVariants.WARM)))).build()))), ItemEntry.builder(Items.BLUE_EGG).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().components(ComponentsPredicate.Builder.create().exact(ComponentMapPredicate.of(DataComponentTypes.CHICKEN_VARIANT, new LazyRegistryEntryReference<ChickenVariant>(lv.getOrThrow(ChickenVariants.COLD)))).build())))}))));
        lootTableBiConsumer.accept(LootTables.ARMADILLO_SHED_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.ARMADILLO_SCUTE))));
        lootTableBiConsumer.accept(LootTables.TURTLE_GROW_GAMEPLAY, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(Items.TURTLE_SCUTE))));
    }
}

