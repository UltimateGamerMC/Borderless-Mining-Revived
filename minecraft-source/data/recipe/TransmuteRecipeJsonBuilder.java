/*
 * External method calls:
 *   Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion;create(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/advancement/AdvancementCriterion;
 *   Lnet/minecraft/advancement/Advancement$Builder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/AdvancementRewards$Builder;recipe(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/advancement/AdvancementRewards$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;rewards(Lnet/minecraft/advancement/AdvancementRewards$Builder;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/advancement/Advancement$Builder;criteriaMerger(Lnet/minecraft/advancement/AdvancementRequirements$CriterionMerger;)Lnet/minecraft/advancement/Advancement$Builder;
 *   Lnet/minecraft/data/recipe/CraftingRecipeJsonBuilder;toCraftingCategory(Lnet/minecraft/recipe/book/RecipeCategory;)Lnet/minecraft/recipe/book/CraftingRecipeCategory;
 *   Lnet/minecraft/util/Identifier;withPrefixedPath(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/advancement/Advancement$Builder;build(Lnet/minecraft/util/Identifier;)Lnet/minecraft/advancement/AdvancementEntry;
 *   Lnet/minecraft/data/recipe/RecipeExporter;accept(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/recipe/Recipe;Lnet/minecraft/advancement/AdvancementEntry;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/recipe/TransmuteRecipeJsonBuilder;validate(Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/data/recipe/TransmuteRecipeJsonBuilder;group(Ljava/lang/String;)Lnet/minecraft/data/recipe/TransmuteRecipeJsonBuilder;
 *   Lnet/minecraft/data/recipe/TransmuteRecipeJsonBuilder;criterion(Ljava/lang/String;Lnet/minecraft/advancement/AdvancementCriterion;)Lnet/minecraft/data/recipe/TransmuteRecipeJsonBuilder;
 */
package net.minecraft.data.recipe;

import java.util.LinkedHashMap;
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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.TransmuteRecipe;
import net.minecraft.recipe.TransmuteRecipeResult;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.jspecify.annotations.Nullable;

public class TransmuteRecipeJsonBuilder
implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final RegistryEntry<Item> result;
    private final Ingredient input;
    private final Ingredient material;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap();
    private @Nullable String group;

    private TransmuteRecipeJsonBuilder(RecipeCategory category, RegistryEntry<Item> result, Ingredient input, Ingredient material) {
        this.category = category;
        this.result = result;
        this.input = input;
        this.material = material;
    }

    public static TransmuteRecipeJsonBuilder create(RecipeCategory category, Ingredient input, Ingredient material, Item result) {
        return new TransmuteRecipeJsonBuilder(category, result.getRegistryEntry(), input, material);
    }

    @Override
    public TransmuteRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> arg) {
        this.criteria.put(string, arg);
        return this;
    }

    @Override
    public TransmuteRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.result.value();
    }

    @Override
    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        Advancement.Builder lv = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(lv::criterion);
        TransmuteRecipe lv2 = new TransmuteRecipe(Objects.requireNonNullElse(this.group, ""), CraftingRecipeJsonBuilder.toCraftingCategory(this.category), this.input, this.material, new TransmuteRecipeResult(this.result.value()));
        exporter.accept(recipeKey, lv2, lv.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(RegistryKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(recipeKey.getValue()));
        }
    }

    @Override
    public /* synthetic */ CraftingRecipeJsonBuilder group(@Nullable String group) {
        return this.group(group);
    }

    public /* synthetic */ CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion criterion) {
        return this.criterion(name, criterion);
    }
}

