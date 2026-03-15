/*
 * External method calls:
 *   Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;
 *   Lnet/minecraft/recipe/Ingredient;ofTag(Lnet/minecraft/registry/entry/RegistryEntryList;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/recipe/Ingredient;ofItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion;create(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/Advancement$Builder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/AdvancementRewards$Builder;recipe(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/advancement/AdvancementRewards$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;rewards(Lnet/minecraft/advancement/AdvancementRewards$Builder;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;criteriaMerger(Lnet/minecraft/advancement/AdvancementRequirements$CriterionMerger;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;toCraftingCategory(Lnet/minecraft/recipe/book/RecipeCategory;)Lnet/minecraft/recipe/book/CraftingRecipeCategory;
 *   Lnet/minecraft/util/Identifier;withPrefixedPath(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/advancement/Advancement$Builder;build(Lnet/minecraft/util/Identifier;)Lnet/minecraft/advancement/AdvancementEntry;
 *   Lnet/minecraft/data/recipe/RecipeExporter;accept(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/recipe/Recipe;Lnet/minecraft/advancement/AdvancementEntry;)V
 *   Lnet/minecraft/recipe/RawShapedRecipe;create(Ljava/util/Map;Ljava/util/List;)Lnet/minecraft/recipe/RawShapedRecipe;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;create(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/recipe/book/RecipeCategory;Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;input(Ljava/lang/Character;Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;validate(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/recipe/RawShapedRecipe;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/ShapedRecipeJsonBuilder;
 */
package net.minecraft.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import org.jspecify.annotations.Nullable;

public class ShapedRecipeJsonBuilder
implements CraftingRecipeJsonBuilder {
    private final RegistryEntryLookup<Item> registryLookup;
    private final RecipeCategory category;
    private final Item output;
    private final int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap();
    private @Nullable String group;
    private boolean showNotification = true;

    private ShapedRecipeJsonBuilder(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output, int count) {
        this.registryLookup = registryLookup;
        this.category = category;
        this.output = output.asItem();
        this.count = count;
    }

    public static ShapedRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output) {
        return ShapedRecipeJsonBuilder.create(registryLookup, category, output, 1);
    }

    public static ShapedRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output, int count) {
        return new ShapedRecipeJsonBuilder(registryLookup, category, output, count);
    }

    public ShapedRecipeJsonBuilder input(Character c, TagKey<Item> tag) {
        return this.input(c, Ingredient.ofTag(this.registryLookup.getOrThrow(tag)));
    }

    public ShapedRecipeJsonBuilder input(Character c, ItemConvertible item) {
        return this.input(c, Ingredient.ofItem(item));
    }

    public ShapedRecipeJsonBuilder input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        }
        if (c.charValue() == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.inputs.put(c, ingredient);
        return this;
    }

    public ShapedRecipeJsonBuilder pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.pattern.add(patternStr);
        return this;
    }

    @Override
    public ShapedRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> arg) {
        this.criteria.put(string, arg);
        return this;
    }

    @Override
    public ShapedRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public ShapedRecipeJsonBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        RawShapedRecipe lv = this.validate(recipeKey);
        Advancement.Builder lv2 = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(lv2::criterion);
        ShapedRecipe lv3 = new ShapedRecipe(Objects.requireNonNullElse(this.group, ""), CraftingRecipeJsonBuilder.toCraftingCategory(this.category), lv, new ItemStack(this.output, this.count), this.showNotification);
        exporter.accept(recipeKey, lv3, lv2.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private RawShapedRecipe validate(RegistryKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(recipeKey.getValue()));
        }
        return RawShapedRecipe.create(this.inputs, this.pattern);
    }

    @Override
    public /* synthetic */ CraftingRecipeJsonBuilder group(@Nullable String group) {
        return this.group(group);
    }

    public /* synthetic */ CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion criterion) {
        return this.criterion(name, criterion);
    }
}

