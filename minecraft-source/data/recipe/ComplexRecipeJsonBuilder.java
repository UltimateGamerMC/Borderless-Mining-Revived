/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;of(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/data/recipe/RecipeExporter;accept(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/recipe/Recipe;Lnet/minecraft/advancement/AdvancementEntry;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/recipe/ComplexRecipeJsonBuilder;offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Lnet/minecraft/registry/RegistryKey;)V
 */
package net.minecraft.data.recipe;

import java.util.function.Function;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ComplexRecipeJsonBuilder {
    private final Function<CraftingRecipeCategory, Recipe<?>> recipeFactory;

    public ComplexRecipeJsonBuilder(Function<CraftingRecipeCategory, Recipe<?>> recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    public static ComplexRecipeJsonBuilder create(Function<CraftingRecipeCategory, Recipe<?>> recipeFactory) {
        return new ComplexRecipeJsonBuilder(recipeFactory);
    }

    public void offerTo(RecipeExporter exporter, String id) {
        this.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(id)));
    }

    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        exporter.accept(recipeKey, this.recipeFactory.apply(CraftingRecipeCategory.MISC), null);
    }
}

