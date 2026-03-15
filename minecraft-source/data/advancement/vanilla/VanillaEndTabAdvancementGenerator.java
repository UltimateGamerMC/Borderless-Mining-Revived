/*
 * External method calls:
 *   Lnet/minecraft/advancement/Advancement$Builder;create()Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/advancement/Advancement$Builder;display(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;Lnet/minecraft/advancement/AdvancementFrame;ZZZ)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/criterion/ChangedDimensionCriterion$Conditions;to(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/Advancement$Builder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;build(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancement/AdvancementEntry;
 *   Lnet/minecraft/advancement/Advancement$Builder;parent(Lnet/minecraft/advancement/AdvancementEntry;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;type(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/advancement/criterion/OnKilledCriterion$Conditions;createPlayerKilledEntity(Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/criterion/EnterBlockCriterion$Conditions;block(Lnet/minecraft/block/Block;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/criterion/SummonedEntityCriterion$Conditions;create(Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/predicate/entity/LocationPredicate$Builder;createStructure(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/predicate/entity/LocationPredicate$Builder;
 *   Lnet/minecraft/advancement/criterion/TickCriterion$Conditions;createLocation(Lnet/minecraft/predicate/entity/LocationPredicate$Builder;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/criterion/InventoryChangedCriterion$Conditions;items([Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/AdvancementRewards$Builder;experience(I)Lnet/minecraft/advancement/AdvancementRewards$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;rewards(Lnet/minecraft/advancement/AdvancementRewards$Builder;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/predicate/NumberRange$DoubleRange;atLeast(D)Lnet/minecraft/predicate/NumberRange$DoubleRange;
 *   Lnet/minecraft/predicate/entity/DistancePredicate;y(Lnet/minecraft/predicate/NumberRange$DoubleRange;)Lnet/minecraft/predicate/entity/DistancePredicate;
 *   Lnet/minecraft/advancement/criterion/LevitationCriterion$Conditions;create(Lnet/minecraft/predicate/entity/DistancePredicate;)Lnet/minecraft/advancement/AdvancementCriterion;
 */
package net.minecraft.data.advancement.vanilla;

import java.util.function.Consumer;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.LevitationCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.SummonedEntityCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.advancement.AdvancementTabGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureKeys;

public class VanillaEndTabAdvancementGenerator
implements AdvancementTabGenerator {
    @Override
    public void accept(RegistryWrapper.WrapperLookup registries, Consumer<AdvancementEntry> exporter) {
        RegistryEntryLookup lv = registries.getOrThrow(RegistryKeys.ENTITY_TYPE);
        AdvancementEntry lv2 = Advancement.Builder.create().display(Blocks.END_STONE, (Text)Text.translatable("advancements.end.root.title"), (Text)Text.translatable("advancements.end.root.description"), Identifier.ofVanilla("gui/advancements/backgrounds/end"), AdvancementFrame.TASK, false, false, false).criterion("entered_end", ChangedDimensionCriterion.Conditions.to(World.END)).build(exporter, "end/root");
        AdvancementEntry lv3 = Advancement.Builder.create().parent(lv2).display(Blocks.DRAGON_HEAD, (Text)Text.translatable("advancements.end.kill_dragon.title"), (Text)Text.translatable("advancements.end.kill_dragon.description"), null, AdvancementFrame.TASK, true, true, false).criterion("killed_dragon", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(lv, EntityType.ENDER_DRAGON))).build(exporter, "end/kill_dragon");
        AdvancementEntry lv4 = Advancement.Builder.create().parent(lv3).display(Items.ENDER_PEARL, (Text)Text.translatable("advancements.end.enter_end_gateway.title"), (Text)Text.translatable("advancements.end.enter_end_gateway.description"), null, AdvancementFrame.TASK, true, true, false).criterion("entered_end_gateway", EnterBlockCriterion.Conditions.block(Blocks.END_GATEWAY)).build(exporter, "end/enter_end_gateway");
        Advancement.Builder.create().parent(lv3).display(Items.END_CRYSTAL, (Text)Text.translatable("advancements.end.respawn_dragon.title"), (Text)Text.translatable("advancements.end.respawn_dragon.description"), null, AdvancementFrame.GOAL, true, true, false).criterion("summoned_dragon", SummonedEntityCriterion.Conditions.create(EntityPredicate.Builder.create().type(lv, EntityType.ENDER_DRAGON))).build(exporter, "end/respawn_dragon");
        AdvancementEntry lv5 = Advancement.Builder.create().parent(lv4).display(Blocks.PURPUR_BLOCK, (Text)Text.translatable("advancements.end.find_end_city.title"), (Text)Text.translatable("advancements.end.find_end_city.description"), null, AdvancementFrame.TASK, true, true, false).criterion("in_city", TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createStructure(registries.getOrThrow(RegistryKeys.STRUCTURE).getOrThrow(StructureKeys.END_CITY)))).build(exporter, "end/find_end_city");
        Advancement.Builder.create().parent(lv3).display(Items.DRAGON_BREATH, (Text)Text.translatable("advancements.end.dragon_breath.title"), (Text)Text.translatable("advancements.end.dragon_breath.description"), null, AdvancementFrame.GOAL, true, true, false).criterion("dragon_breath", InventoryChangedCriterion.Conditions.items(Items.DRAGON_BREATH)).build(exporter, "end/dragon_breath");
        Advancement.Builder.create().parent(lv5).display(Items.SHULKER_SHELL, (Text)Text.translatable("advancements.end.levitate.title"), (Text)Text.translatable("advancements.end.levitate.description"), null, AdvancementFrame.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).criterion("levitated", LevitationCriterion.Conditions.create(DistancePredicate.y(NumberRange.DoubleRange.atLeast(50.0)))).build(exporter, "end/levitate");
        Advancement.Builder.create().parent(lv5).display(Items.ELYTRA, (Text)Text.translatable("advancements.end.elytra.title"), (Text)Text.translatable("advancements.end.elytra.description"), null, AdvancementFrame.GOAL, true, true, false).criterion("elytra", InventoryChangedCriterion.Conditions.items(Items.ELYTRA)).build(exporter, "end/elytra");
        Advancement.Builder.create().parent(lv3).display(Blocks.DRAGON_EGG, (Text)Text.translatable("advancements.end.dragon_egg.title"), (Text)Text.translatable("advancements.end.dragon_egg.description"), null, AdvancementFrame.GOAL, true, true, false).criterion("dragon_egg", InventoryChangedCriterion.Conditions.items(Blocks.DRAGON_EGG)).build(exporter, "end/dragon_egg");
    }
}

