/*
 * External method calls:
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;input(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V
 *   Lnet/minecraft/recipe/Ingredient;ofItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;create(Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;FILnet/minecraft/recipe/RecipeSerializer;Lnet/minecraft/recipe/AbstractCookingRecipe$RecipeFactory;)Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/SmithingTransformRecipeJsonBuilder;create(Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/Item;)Lnet/minecraft/data/recipe/SmithingTransformRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/SmithingTransformRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/SmithingTransformRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/SmithingTransformRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/SmithingTrimRecipeJsonBuilder;create(Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/recipe/book/RecipeCategory;)Lnet/minecraft/data/recipe/SmithingTrimRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/SmithingTrimRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/SmithingTrimRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/SmithingTrimRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;input(Ljava/lang/Character;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;pattern(Ljava/lang/String;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;)V
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;input(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;)V
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;input(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;input(Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;input(Ljava/lang/Character;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;)V
 *   Lnet/minecraft/recipe/Ingredient;ofItems(Ljava/util/stream/Stream;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;input(Ljava/lang/Character;Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/StonecuttingRecipeJsonBuilder;createStonecutting(Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/StonecuttingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/StonecuttingRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/StonecuttingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/StonecuttingRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;createSmelting(Lnet/minecraft/recipe/Ingredient;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;FI)Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CookingRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;)V
 *   Lnet/minecraft/util/Identifier;of(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/component/ComponentChanges;builder()Lnet/minecraft/component/ComponentChanges$Builder;
 *   Lnet/minecraft/component/ComponentChanges$Builder;build()Lnet/minecraft/component/ComponentChanges;
 *   Lnet/minecraft/advancement/criterion/EnterBlockCriterion;create(Lnet/minecraft/advancement/criterion/CriterionConditions;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/predicate/item/ItemPredicate$Builder;create()Lnet/minecraft/predicate/item/ItemPredicate$Builder;
 *   Lnet/minecraft/predicate/item/ItemPredicate$Builder;items(Lnet/minecraft/registry/RegistryEntryLookup;[Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/predicate/item/ItemPredicate$Builder;
 *   Lnet/minecraft/predicate/item/ItemPredicate$Builder;count(Lnet/minecraft/predicate/NumberRange$IntRange;)Lnet/minecraft/predicate/item/ItemPredicate$Builder;
 *   Lnet/minecraft/predicate/item/ItemPredicate$Builder;tag(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/predicate/item/ItemPredicate$Builder;
 *   Lnet/minecraft/advancement/criterion/InventoryChangedCriterion;create(Lnet/minecraft/advancement/criterion/CriterionConditions;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;
 *   Lnet/minecraft/recipe/Ingredient;ofTag(Lnet/minecraft/registry/entry/RegistryEntryList;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator$BlockFamilyRecipeFactory;create(Lnet/minecraft/data/recipe/RecipeGenerator;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerShapelessRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;Ljava/lang/String;I)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createShapeless(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;conditionsFromItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;convertBetween(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;)Ljava/lang/String;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerMultipleOptions(Lnet/minecraft/recipe/RecipeSerializer;Lnet/minecraft/recipe/AbstractCookingRecipe$RecipeFactory;Ljava/util/List;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;FILjava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;ingredientFromTag(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;conditionsFromTag(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createShaped(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createShapeless(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerCompactingRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createShaped(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;requireEnteringFluid(Lnet/minecraft/block/Block;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createPressurePlateRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createSlabRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerDyeablesRecipes(Ljava/util/List;Ljava/util/List;Lnet/minecraft/item/Item;Ljava/lang/String;Lnet/minecraft/recipe/book/RecipeCategory;)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createCondensingRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createCutCopperRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createChiseledBlockRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerStonecuttingRecipe(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;I)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerReversibleCompactingRecipes(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerFoodCookingRecipe(Ljava/lang/String;Lnet/minecraft/recipe/RecipeSerializer;Lnet/minecraft/recipe/AbstractCookingRecipe$RecipeFactory;ILnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;F)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createShapeless(Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/data/recipe/ShapelessRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;conditionsFromPredicates([Lnet/minecraft/predicate/item/ItemPredicate$Builder;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;conditionsFromItemPredicates([Lnet/minecraft/predicate/item/ItemPredicate;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createTrapdoorRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createStairsRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createSignRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createFenceGateRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createFenceRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createDoorRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;createButtonRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/RecipeGenerator;offerCrackingRecipe(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/item/ItemConvertible;)V
 *   Lnet/minecraft/data/recipe/RecipeGenerator;generateFamily(Lnet/minecraft/data/family/BlockFamily;Lnet/minecraft/resource/featuretoggle/FeatureSet;)V
 */
package net.minecraft.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.data.recipe.SmithingTrimRecipeJsonBuilder;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class RecipeGenerator {
    protected final RegistryWrapper.WrapperLookup registries;
    private final RegistryEntryLookup<Item> itemLookup;
    protected final RecipeExporter exporter;
    private static final Map<BlockFamily.Variant, BlockFamilyRecipeFactory> VARIANT_FACTORIES = ImmutableMap.builder().put(BlockFamily.Variant.BUTTON, (generator, output, input) -> generator.createButtonRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.CHISELED, (generator, output, input) -> generator.createChiseledBlockRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItem(input))).put(BlockFamily.Variant.CUT, (generator, output, input) -> generator.createCutCopperRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItem(input))).put(BlockFamily.Variant.DOOR, (generator, output, input) -> generator.createDoorRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.CUSTOM_FENCE, (generator, output, input) -> generator.createFenceRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.FENCE, (generator, output, input) -> generator.createFenceRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.CUSTOM_FENCE_GATE, (generator, output, input) -> generator.createFenceGateRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.FENCE_GATE, (generator, output, input) -> generator.createFenceGateRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.SIGN, (generator, output, input) -> generator.createSignRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.SLAB, (generator, output, input) -> generator.createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItem(input))).put(BlockFamily.Variant.STAIRS, (generator, output, input) -> generator.createStairsRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.PRESSURE_PLATE, (generator, output, input) -> generator.createPressurePlateRecipe(RecipeCategory.REDSTONE, output, Ingredient.ofItem(input))).put(BlockFamily.Variant.POLISHED, (generator, output, input) -> generator.createCondensingRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItem(input))).put(BlockFamily.Variant.TRAPDOOR, (generator, output, input) -> generator.createTrapdoorRecipe(output, Ingredient.ofItem(input))).put(BlockFamily.Variant.WALL, (generator, output, input) -> generator.getWallRecipe(RecipeCategory.DECORATIONS, output, Ingredient.ofItem(input))).build();

    protected RecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        this.registries = registries;
        this.itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
        this.exporter = exporter;
    }

    protected abstract void generate();

    protected void generateFamilies(FeatureSet enabledFeatures) {
        BlockFamilies.getFamilies().filter(BlockFamily::shouldGenerateRecipes).forEach(family -> this.generateFamily((BlockFamily)family, enabledFeatures));
    }

    protected void offerSingleOutputShapelessRecipe(ItemConvertible output, ItemConvertible input, @Nullable String group) {
        this.offerShapelessRecipe(output, input, group, 1);
    }

    protected void offerShapelessRecipe(ItemConvertible output, ItemConvertible input, @Nullable String group, int outputCount) {
        this.createShapeless(RecipeCategory.MISC, output, outputCount).input(input).group(group).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter, RecipeGenerator.convertBetween(output, input));
    }

    protected void offerSmelting(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        this.offerMultipleOptions(RecipeSerializer.SMELTING, SmeltingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smelting");
    }

    protected void offerBlasting(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
        this.offerMultipleOptions(RecipeSerializer.BLASTING, BlastingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_blasting");
    }

    private <T extends AbstractCookingRecipe> void offerMultipleOptions(RecipeSerializer<T> serializer, AbstractCookingRecipe.RecipeFactory<T> recipeFactory, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group, String suffix) {
        for (ItemConvertible lv : inputs) {
            CookingRecipeJsonBuilder.create(Ingredient.ofItem(lv), category, output, experience, cookingTime, serializer, recipeFactory).group(group).criterion(RecipeGenerator.hasItem(lv), (AdvancementCriterion)this.conditionsFromItem(lv)).offerTo(this.exporter, RecipeGenerator.getItemPath(output) + suffix + "_" + RecipeGenerator.getItemPath(lv));
        }
    }

    protected void offerNetheriteUpgradeRecipe(Item input, RecipeCategory category, Item result) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItem(input), this.ingredientFromTag(ItemTags.NETHERITE_TOOL_MATERIALS), category, result).criterion("has_netherite_ingot", this.conditionsFromTag(ItemTags.NETHERITE_TOOL_MATERIALS)).offerTo(this.exporter, RecipeGenerator.getItemPath(result) + "_smithing");
    }

    protected void offerSmithingTrimRecipe(Item input, RegistryKey<ArmorTrimPattern> pattern, RegistryKey<Recipe<?>> recipeKey) {
        RegistryEntry.Reference<ArmorTrimPattern> lv = this.registries.getOrThrow(RegistryKeys.TRIM_PATTERN).getOrThrow(pattern);
        SmithingTrimRecipeJsonBuilder.create(Ingredient.ofItem(input), this.ingredientFromTag(ItemTags.TRIMMABLE_ARMOR), this.ingredientFromTag(ItemTags.TRIM_MATERIALS), lv, RecipeCategory.MISC).criterion("has_smithing_trim_template", this.conditionsFromItem(input)).offerTo(this.exporter, recipeKey);
    }

    protected void offer2x2CompactingRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createShaped(category, output, 1).input(Character.valueOf('#'), input).pattern("##").pattern("##").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerCompactingRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input, String criterionName) {
        this.createShapeless(category, output).input(input, 9).criterion(criterionName, (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerCompactingRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.offerCompactingRecipe(category, output, input, RecipeGenerator.hasItem(input));
    }

    protected void offerPlanksRecipe2(ItemConvertible output, TagKey<Item> logTag, int count) {
        this.createShapeless(RecipeCategory.BUILDING_BLOCKS, output, count).input(logTag).group("planks").criterion("has_log", (AdvancementCriterion)this.conditionsFromTag(logTag)).offerTo(this.exporter);
    }

    protected void offerPlanksRecipe(ItemConvertible output, TagKey<Item> logTag, int count) {
        this.createShapeless(RecipeCategory.BUILDING_BLOCKS, output, count).input(logTag).group("planks").criterion("has_logs", (AdvancementCriterion)this.conditionsFromTag(logTag)).offerTo(this.exporter);
    }

    protected void offerBarkBlockRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 3).input(Character.valueOf('#'), input).pattern("##").pattern("##").group("bark").criterion("has_log", (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerBoatRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.TRANSPORTATION, output).input(Character.valueOf('#'), input).pattern("# #").pattern("###").group("boat").criterion("in_water", (AdvancementCriterion)RecipeGenerator.requireEnteringFluid(Blocks.WATER)).offerTo(this.exporter);
    }

    protected void offerChestBoatRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShapeless(RecipeCategory.TRANSPORTATION, output).input(Blocks.CHEST).input(input).group("chest_boat").criterion("has_boat", (AdvancementCriterion)this.conditionsFromTag(ItemTags.BOATS)).offerTo(this.exporter);
    }

    private CraftingRecipeJsonBuilder createButtonRecipe(ItemConvertible output, Ingredient input) {
        return this.createShapeless(RecipeCategory.REDSTONE, output).input(input);
    }

    protected CraftingRecipeJsonBuilder createDoorRecipe(ItemConvertible output, Ingredient input) {
        return this.createShaped(RecipeCategory.REDSTONE, output, 3).input(Character.valueOf('#'), input).pattern("##").pattern("##").pattern("##");
    }

    private CraftingRecipeJsonBuilder createFenceRecipe(ItemConvertible output, Ingredient input) {
        int i = output == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        Item lv = output == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK;
        return this.createShaped(RecipeCategory.DECORATIONS, output, i).input(Character.valueOf('W'), input).input(Character.valueOf('#'), lv).pattern("W#W").pattern("W#W");
    }

    private CraftingRecipeJsonBuilder createFenceGateRecipe(ItemConvertible output, Ingredient input) {
        return this.createShaped(RecipeCategory.REDSTONE, output).input(Character.valueOf('#'), Items.STICK).input(Character.valueOf('W'), input).pattern("#W#").pattern("#W#");
    }

    protected void offerPressurePlateRecipe(ItemConvertible output, ItemConvertible input) {
        this.createPressurePlateRecipe(RecipeCategory.REDSTONE, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    private CraftingRecipeJsonBuilder createPressurePlateRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output).input(Character.valueOf('#'), input).pattern("##");
    }

    protected void offerSlabRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createSlabRecipe(category, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerShelfRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.DECORATIONS, output, 6).input(Character.valueOf('#'), input).pattern("###").pattern("   ").pattern("###").group("shelf").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected CraftingRecipeJsonBuilder createSlabRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output, 6).input(Character.valueOf('#'), input).pattern("###");
    }

    protected CraftingRecipeJsonBuilder createStairsRecipe(ItemConvertible output, Ingredient input) {
        return this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 4).input(Character.valueOf('#'), input).pattern("#  ").pattern("## ").pattern("###");
    }

    protected CraftingRecipeJsonBuilder createTrapdoorRecipe(ItemConvertible output, Ingredient input) {
        return this.createShaped(RecipeCategory.REDSTONE, output, 2).input(Character.valueOf('#'), input).pattern("###").pattern("###");
    }

    private CraftingRecipeJsonBuilder createSignRecipe(ItemConvertible output, Ingredient input) {
        return this.createShaped(RecipeCategory.DECORATIONS, output, 3).group("sign").input(Character.valueOf('#'), input).input(Character.valueOf('X'), Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    protected void offerHangingSignRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.DECORATIONS, output, 6).group("hanging_sign").input(Character.valueOf('#'), input).input(Character.valueOf('X'), Items.IRON_CHAIN).pattern("X X").pattern("###").pattern("###").criterion("has_stripped_logs", (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerDyeableRecipes(List<Item> dyes, List<Item> dyeables, String group, RecipeCategory category) {
        this.offerDyeablesRecipes(dyes, dyeables, null, group, category);
    }

    protected void offerDyeablesRecipes(List<Item> dyes, List<Item> dyeables, @Nullable Item undyed, String group, RecipeCategory category) {
        for (int i = 0; i < dyes.size(); ++i) {
            Item lv = dyes.get(i);
            Item lv2 = dyeables.get(i);
            Stream<Item> stream = dyeables.stream().filter(item -> !item.equals(lv2));
            if (undyed != null) {
                stream = Stream.concat(stream, Stream.of(undyed));
            }
            this.createShapeless(category, lv2).input(lv).input(Ingredient.ofItems(stream)).group(group).criterion("has_needed_dye", (AdvancementCriterion)this.conditionsFromItem(lv)).offerTo(this.exporter, "dye_" + RecipeGenerator.getItemPath(lv2));
        }
    }

    protected void offerCarpetRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.DECORATIONS, output, 3).input(Character.valueOf('#'), input).pattern("##").group("carpet").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerBedRecipe(ItemConvertible output, ItemConvertible inputWool) {
        this.createShaped(RecipeCategory.DECORATIONS, output).input(Character.valueOf('#'), inputWool).input(Character.valueOf('X'), ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").criterion(RecipeGenerator.hasItem(inputWool), (AdvancementCriterion)this.conditionsFromItem(inputWool)).offerTo(this.exporter);
    }

    protected void offerBannerRecipe(ItemConvertible output, ItemConvertible inputWool) {
        this.createShaped(RecipeCategory.DECORATIONS, output).input(Character.valueOf('#'), inputWool).input(Character.valueOf('|'), Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").criterion(RecipeGenerator.hasItem(inputWool), (AdvancementCriterion)this.conditionsFromItem(inputWool)).offerTo(this.exporter);
    }

    protected void offerStainedGlassDyeingRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 8).input(Character.valueOf('#'), Blocks.GLASS).input(Character.valueOf('X'), input).pattern("###").pattern("#X#").pattern("###").group("stained_glass").criterion("has_glass", (AdvancementCriterion)this.conditionsFromItem(Blocks.GLASS)).offerTo(this.exporter);
    }

    protected void offerDriedGhast(ItemConvertible output) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 1).input(Character.valueOf('#'), Items.GHAST_TEAR).input(Character.valueOf('X'), Items.SOUL_SAND).pattern("###").pattern("#X#").pattern("###").group("dry_ghast").criterion(RecipeGenerator.hasItem(Items.GHAST_TEAR), (AdvancementCriterion)this.conditionsFromItem(Items.GHAST_TEAR)).offerTo(this.exporter);
    }

    protected void offerHarness(ItemConvertible output, ItemConvertible wool) {
        this.createShaped(RecipeCategory.COMBAT, output).input(Character.valueOf('#'), wool).input(Character.valueOf('G'), Items.GLASS).input(Character.valueOf('L'), Items.LEATHER).pattern("LLL").pattern("G#G").group("harness").criterion("has_dried_ghast", (AdvancementCriterion)this.conditionsFromItem(Blocks.DRIED_GHAST)).offerTo(this.exporter);
    }

    protected void offerStainedGlassPaneRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.DECORATIONS, output, 16).input(Character.valueOf('#'), input).pattern("###").pattern("###").group("stained_glass_pane").criterion("has_glass", (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerStainedGlassPaneDyeingRecipe(ItemConvertible output, ItemConvertible inputDye) {
        ((ShapedRecipeJsonBuilder)this.createShaped(RecipeCategory.DECORATIONS, output, 8).input(Character.valueOf('#'), Blocks.GLASS_PANE).input(Character.valueOf('$'), inputDye).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").criterion("has_glass_pane", (AdvancementCriterion)this.conditionsFromItem(Blocks.GLASS_PANE))).criterion(RecipeGenerator.hasItem(inputDye), (AdvancementCriterion)this.conditionsFromItem(inputDye)).offerTo(this.exporter, RecipeGenerator.convertBetween(output, Blocks.GLASS_PANE));
    }

    protected void offerTerracottaDyeingRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 8).input(Character.valueOf('#'), Blocks.TERRACOTTA).input(Character.valueOf('X'), input).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").criterion("has_terracotta", (AdvancementCriterion)this.conditionsFromItem(Blocks.TERRACOTTA)).offerTo(this.exporter);
    }

    protected void offerConcretePowderDyeingRecipe(ItemConvertible output, ItemConvertible input) {
        ((ShapelessRecipeJsonBuilder)this.createShapeless(RecipeCategory.BUILDING_BLOCKS, output, 8).input(input).input(Blocks.SAND, 4).input(Blocks.GRAVEL, 4).group("concrete_powder").criterion("has_sand", (AdvancementCriterion)this.conditionsFromItem(Blocks.SAND))).criterion("has_gravel", (AdvancementCriterion)this.conditionsFromItem(Blocks.GRAVEL)).offerTo(this.exporter);
    }

    protected void offerCandleDyeingRecipe(ItemConvertible output, ItemConvertible input) {
        this.createShapeless(RecipeCategory.DECORATIONS, output).input(Blocks.CANDLE).input(input).group("dyed_candle").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerWallRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.getWallRecipe(category, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    private CraftingRecipeJsonBuilder getWallRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output, 6).input(Character.valueOf('#'), input).pattern("###").pattern("###");
    }

    protected void offerPolishedStoneRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createCondensingRecipe(category, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    private CraftingRecipeJsonBuilder createCondensingRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output, 4).input(Character.valueOf('S'), input).pattern("SS").pattern("SS");
    }

    protected void offerCutCopperRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createCutCopperRecipe(category, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    private ShapedRecipeJsonBuilder createCutCopperRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output, 4).input(Character.valueOf('#'), input).pattern("##").pattern("##");
    }

    protected void offerChiseledBlockRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createChiseledBlockRecipe(category, output, Ingredient.ofItem(input)).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerMosaicRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.createShaped(category, output).input(Character.valueOf('#'), input).pattern("#").pattern("#").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected ShapedRecipeJsonBuilder createChiseledBlockRecipe(RecipeCategory category, ItemConvertible output, Ingredient input) {
        return this.createShaped(category, output).input(Character.valueOf('#'), input).pattern("#").pattern("#");
    }

    protected void offerStonecuttingRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        this.offerStonecuttingRecipe(category, output, input, 1);
    }

    protected void offerStonecuttingRecipe(RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItem(input), category, output, count).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter, RecipeGenerator.convertBetween(output, input) + "_stonecutting");
    }

    private void offerCrackingRecipe(ItemConvertible output, ItemConvertible input) {
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItem(input), RecipeCategory.BUILDING_BLOCKS, output, 0.1f, 200).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerReversibleCompactingRecipes(RecipeCategory reverseCategory, ItemConvertible baseItem, RecipeCategory compactingCategory, ItemConvertible compactItem) {
        this.offerReversibleCompactingRecipes(reverseCategory, baseItem, compactingCategory, compactItem, RecipeGenerator.getRecipeName(compactItem), null, RecipeGenerator.getRecipeName(baseItem), null);
    }

    protected void offerReversibleCompactingRecipesWithCompactingRecipeGroup(RecipeCategory reverseCategory, ItemConvertible baseItem, RecipeCategory compactingCategory, ItemConvertible compactItem, String compactingId, String compactingGroup) {
        this.offerReversibleCompactingRecipes(reverseCategory, baseItem, compactingCategory, compactItem, compactingId, compactingGroup, RecipeGenerator.getRecipeName(baseItem), null);
    }

    protected void offerReversibleCompactingRecipesWithReverseRecipeGroup(RecipeCategory reverseCategory, ItemConvertible baseItem, RecipeCategory compactingCategory, ItemConvertible compactItem, String reverseId, String reverseGroup) {
        this.offerReversibleCompactingRecipes(reverseCategory, baseItem, compactingCategory, compactItem, RecipeGenerator.getRecipeName(compactItem), null, reverseId, reverseGroup);
    }

    private void offerReversibleCompactingRecipes(RecipeCategory reverseCategory, ItemConvertible baseItem, RecipeCategory compactingCategory, ItemConvertible compactItem, String compactingId, @Nullable String compactingGroup, String reverseId, @Nullable String reverseGroup) {
        ((ShapelessRecipeJsonBuilder)this.createShapeless(reverseCategory, baseItem, 9).input(compactItem).group(reverseGroup).criterion(RecipeGenerator.hasItem(compactItem), (AdvancementCriterion)this.conditionsFromItem(compactItem))).offerTo(this.exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(reverseId)));
        ((ShapedRecipeJsonBuilder)this.createShaped(compactingCategory, compactItem).input(Character.valueOf('#'), baseItem).pattern("###").pattern("###").pattern("###").group(compactingGroup).criterion(RecipeGenerator.hasItem(baseItem), (AdvancementCriterion)this.conditionsFromItem(baseItem))).offerTo(this.exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(compactingId)));
    }

    protected void offerSmithingTemplateCopyingRecipe(ItemConvertible template, ItemConvertible resource) {
        this.createShaped(RecipeCategory.MISC, template, 2).input(Character.valueOf('#'), Items.DIAMOND).input(Character.valueOf('C'), resource).input(Character.valueOf('S'), template).pattern("#S#").pattern("#C#").pattern("###").criterion(RecipeGenerator.hasItem(template), (AdvancementCriterion)this.conditionsFromItem(template)).offerTo(this.exporter);
    }

    protected void offerSmithingTemplateCopyingRecipe(ItemConvertible template, Ingredient resource) {
        this.createShaped(RecipeCategory.MISC, template, 2).input(Character.valueOf('#'), Items.DIAMOND).input(Character.valueOf('C'), resource).input(Character.valueOf('S'), template).pattern("#S#").pattern("#C#").pattern("###").criterion(RecipeGenerator.hasItem(template), (AdvancementCriterion)this.conditionsFromItem(template)).offerTo(this.exporter);
    }

    protected <T extends AbstractCookingRecipe> void generateCookingRecipes(String cooker, RecipeSerializer<T> serializer, AbstractCookingRecipe.RecipeFactory<T> recipeFactory, int cookingTime) {
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.BEEF, Items.COOKED_BEEF, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.COD, Items.COOKED_COD, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.KELP, Items.DRIED_KELP, 0.1f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.SALMON, Items.COOKED_SALMON, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.MUTTON, Items.COOKED_MUTTON, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.POTATO, Items.BAKED_POTATO, 0.35f);
        this.offerFoodCookingRecipe(cooker, serializer, recipeFactory, cookingTime, Items.RABBIT, Items.COOKED_RABBIT, 0.35f);
    }

    private <T extends AbstractCookingRecipe> void offerFoodCookingRecipe(String cooker, RecipeSerializer<T> serializer, AbstractCookingRecipe.RecipeFactory<T> recipeFactory, int cookingTime, ItemConvertible input, ItemConvertible output, float experience) {
        CookingRecipeJsonBuilder.create(Ingredient.ofItem(input), RecipeCategory.FOOD, output, experience, cookingTime, serializer, recipeFactory).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter, RecipeGenerator.getItemPath(output) + "_from_" + cooker);
    }

    protected void offerWaxingRecipes(FeatureSet enabledFeatures) {
        HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get().forEach((unwaxed, waxed) -> {
            if (!waxed.getRequiredFeatures().isSubsetOf(enabledFeatures)) {
                return;
            }
            Pair<RecipeCategory, String> pair = HoneycombItem.WAXED_RECIPE_GROUPS.getOrDefault(waxed, Pair.of(RecipeCategory.BUILDING_BLOCKS, RecipeGenerator.getItemPath(waxed)));
            RecipeCategory lv = pair.getFirst();
            String string = pair.getSecond();
            this.createShapeless(lv, (ItemConvertible)waxed).input((ItemConvertible)unwaxed).input(Items.HONEYCOMB).group(string).criterion(RecipeGenerator.hasItem(unwaxed), (AdvancementCriterion)this.conditionsFromItem((ItemConvertible)unwaxed)).offerTo(this.exporter, RecipeGenerator.convertBetween(waxed, Items.HONEYCOMB));
        });
    }

    protected void offerGrateRecipe(Block output, Block input) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output, 4).input(Character.valueOf('M'), input).pattern(" M ").pattern("M M").pattern(" M ").group(RecipeGenerator.getItemPath(output)).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerBulbRecipe(Block output, Block input) {
        ((ShapedRecipeJsonBuilder)this.createShaped(RecipeCategory.REDSTONE, output, 4).input(Character.valueOf('C'), input).input(Character.valueOf('R'), Items.REDSTONE).input(Character.valueOf('B'), Items.BLAZE_ROD).pattern(" C ").pattern("CBC").pattern(" R ").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input))).group(RecipeGenerator.getItemPath(output)).offerTo(this.exporter);
    }

    protected void offerWaxedChiseledCopperRecipe(Block output, Block input) {
        this.createShaped(RecipeCategory.BUILDING_BLOCKS, output).input(Character.valueOf('M'), input).pattern(" M ").pattern(" M ").group(RecipeGenerator.getItemPath(output)).criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter);
    }

    protected void offerSuspiciousStewRecipe(Item input, SuspiciousStewIngredient stewIngredient) {
        ItemStack lv = new ItemStack(Items.SUSPICIOUS_STEW.getRegistryEntry(), 1, ComponentChanges.builder().add(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, stewIngredient.getStewEffects()).build());
        this.createShapeless(RecipeCategory.FOOD, lv).input(Items.BOWL).input(Items.BROWN_MUSHROOM).input(Items.RED_MUSHROOM).input(input).group("suspicious_stew").criterion(RecipeGenerator.hasItem(input), (AdvancementCriterion)this.conditionsFromItem(input)).offerTo(this.exporter, RecipeGenerator.getItemPath(lv.getItem()) + "_from_" + RecipeGenerator.getItemPath(input));
    }

    protected void generateFamily(BlockFamily family, FeatureSet enabledFeatures) {
        family.getVariants().forEach((variant, block) -> {
            if (!block.getRequiredFeatures().isSubsetOf(enabledFeatures)) {
                return;
            }
            BlockFamilyRecipeFactory lv = VARIANT_FACTORIES.get(variant);
            Block lv2 = this.getVariantRecipeInput(family, (BlockFamily.Variant)((Object)variant));
            if (lv != null) {
                CraftingRecipeJsonBuilder lv3 = lv.create(this, (ItemConvertible)block, lv2);
                family.getGroup().ifPresent(group -> lv3.group(group + (String)(variant == BlockFamily.Variant.CUT ? "" : "_" + variant.getName())));
                lv3.criterion(family.getUnlockCriterionName().orElseGet(() -> RecipeGenerator.hasItem(lv2)), this.conditionsFromItem(lv2));
                lv3.offerTo(this.exporter);
            }
            if (variant == BlockFamily.Variant.CRACKED) {
                this.offerCrackingRecipe((ItemConvertible)block, lv2);
            }
        });
    }

    private Block getVariantRecipeInput(BlockFamily family, BlockFamily.Variant variant) {
        if (variant == BlockFamily.Variant.CHISELED) {
            if (!family.getVariants().containsKey((Object)BlockFamily.Variant.SLAB)) {
                throw new IllegalStateException("Slab is not defined for the family.");
            }
            return family.getVariant(BlockFamily.Variant.SLAB);
        }
        return family.getBaseBlock();
    }

    private static AdvancementCriterion<EnterBlockCriterion.Conditions> requireEnteringFluid(Block block) {
        return Criteria.ENTER_BLOCK.create(new EnterBlockCriterion.Conditions(Optional.empty(), Optional.of(block.getRegistryEntry()), Optional.empty()));
    }

    private AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromItem(NumberRange.IntRange count, ItemConvertible item) {
        return RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(this.itemLookup, item).count(count));
    }

    protected AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromItem(ItemConvertible item) {
        return RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(this.itemLookup, item));
    }

    protected AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromTag(TagKey<Item> tag) {
        return RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().tag(this.itemLookup, tag));
    }

    private static AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromPredicates(ItemPredicate.Builder ... predicates) {
        return RecipeGenerator.conditionsFromItemPredicates((ItemPredicate[])Arrays.stream(predicates).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    private static AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromItemPredicates(ItemPredicate ... predicates) {
        return Criteria.INVENTORY_CHANGED.create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, List.of(predicates)));
    }

    protected static String hasItem(ItemConvertible item) {
        return "has_" + RecipeGenerator.getItemPath(item);
    }

    protected static String getItemPath(ItemConvertible item) {
        return Registries.ITEM.getId(item.asItem()).getPath();
    }

    protected static String getRecipeName(ItemConvertible item) {
        return RecipeGenerator.getItemPath(item);
    }

    protected static String convertBetween(ItemConvertible to, ItemConvertible from) {
        return RecipeGenerator.getItemPath(to) + "_from_" + RecipeGenerator.getItemPath(from);
    }

    protected static String getSmeltingItemPath(ItemConvertible item) {
        return RecipeGenerator.getItemPath(item) + "_from_smelting";
    }

    protected static String getBlastingItemPath(ItemConvertible item) {
        return RecipeGenerator.getItemPath(item) + "_from_blasting";
    }

    protected Ingredient ingredientFromTag(TagKey<Item> tag) {
        return Ingredient.ofTag(this.itemLookup.getOrThrow(tag));
    }

    protected ShapedRecipeJsonBuilder createShaped(RecipeCategory category, ItemConvertible output) {
        return ShapedRecipeJsonBuilder.create(this.itemLookup, category, output);
    }

    protected ShapedRecipeJsonBuilder createShaped(RecipeCategory category, ItemConvertible output, int count) {
        return ShapedRecipeJsonBuilder.create(this.itemLookup, category, output, count);
    }

    protected ShapelessRecipeJsonBuilder createShapeless(RecipeCategory category, ItemStack output) {
        return ShapelessRecipeJsonBuilder.create(this.itemLookup, category, output);
    }

    protected ShapelessRecipeJsonBuilder createShapeless(RecipeCategory category, ItemConvertible output) {
        return ShapelessRecipeJsonBuilder.create(this.itemLookup, category, output);
    }

    protected ShapelessRecipeJsonBuilder createShapeless(RecipeCategory category, ItemConvertible output, int count) {
        return ShapelessRecipeJsonBuilder.create(this.itemLookup, category, output, count);
    }

    @FunctionalInterface
    static interface BlockFamilyRecipeFactory {
        public CraftingRecipeJsonBuilder create(RecipeGenerator var1, ItemConvertible var2, ItemConvertible var3);
    }

    protected static abstract class RecipeProvider
    implements DataProvider {
        private final DataOutput output;
        private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

        protected RecipeProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            this.output = output;
            this.registriesFuture = registriesFuture;
        }

        @Override
        public final CompletableFuture<?> run(final DataWriter writer) {
            return this.registriesFuture.thenCompose(registries -> {
                DataOutput.PathResolver lv = this.output.getResolver(RegistryKeys.RECIPE);
                DataOutput.PathResolver lv2 = this.output.getResolver(RegistryKeys.ADVANCEMENT);
                final HashSet set = Sets.newHashSet();
                final ArrayList list = new ArrayList();
                RecipeExporter lv3 = new RecipeExporter(){
                    final /* synthetic */ RegistryWrapper.WrapperLookup registries;
                    final /* synthetic */ DataOutput.PathResolver recipePathResolver;
                    final /* synthetic */ DataOutput.PathResolver recipeAdvancementPathResolver;
                    {
                        this.registries = arg3;
                        this.recipePathResolver = arg4;
                        this.recipeAdvancementPathResolver = arg5;
                    }

                    @Override
                    public void accept(RegistryKey<Recipe<?>> key, Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
                        if (!set.add(key)) {
                            throw new IllegalStateException("Duplicate recipe " + String.valueOf(key.getValue()));
                        }
                        this.addRecipe(key, recipe);
                        if (advancement != null) {
                            this.addRecipeAdvancement(advancement);
                        }
                    }

                    @Override
                    public Advancement.Builder getAdvancementBuilder() {
                        return Advancement.Builder.createUntelemetered().parent(CraftingRecipeJsonBuilder.ROOT);
                    }

                    @Override
                    public void addRootAdvancement() {
                        AdvancementEntry lv = Advancement.Builder.createUntelemetered().criterion("impossible", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions())).build(CraftingRecipeJsonBuilder.ROOT);
                        this.addRecipeAdvancement(lv);
                    }

                    private void addRecipe(RegistryKey<Recipe<?>> key, Recipe<?> recipe) {
                        list.add(DataProvider.writeCodecToPath(writer, this.registries, Recipe.CODEC, recipe, this.recipePathResolver.resolveJson(key.getValue())));
                    }

                    private void addRecipeAdvancement(AdvancementEntry advancementEntry) {
                        list.add(DataProvider.writeCodecToPath(writer, this.registries, Advancement.CODEC, advancementEntry.value(), this.recipeAdvancementPathResolver.resolveJson(advancementEntry.id())));
                    }
                };
                this.getRecipeGenerator((RegistryWrapper.WrapperLookup)registries, lv3).generate();
                return CompletableFuture.allOf((CompletableFuture[])list.toArray(CompletableFuture[]::new));
            });
        }

        protected abstract RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup var1, RecipeExporter var2);
    }
}

