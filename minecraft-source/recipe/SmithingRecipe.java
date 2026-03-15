/*
 * External method calls:
 *   Lnet/minecraft/recipe/input/SmithingRecipeInput;template()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/recipe/Ingredient;matches(Ljava/util/Optional;Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/recipe/input/SmithingRecipeInput;base()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/recipe/input/SmithingRecipeInput;addition()Lnet/minecraft/item/ItemStack;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/SmithingRecipe;template()Ljava/util/Optional;
 *   Lnet/minecraft/recipe/SmithingRecipe;base()Lnet/minecraft/recipe/Ingredient;
 *   Lnet/minecraft/recipe/SmithingRecipe;addition()Ljava/util/Optional;
 *   Lnet/minecraft/recipe/SmithingRecipe;matches(Lnet/minecraft/recipe/input/SmithingRecipeInput;Lnet/minecraft/world/World;)Z
 */
package net.minecraft.recipe;

import java.util.Optional;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.world.World;

public interface SmithingRecipe
extends Recipe<SmithingRecipeInput> {
    @Override
    default public RecipeType<SmithingRecipe> getType() {
        return RecipeType.SMITHING;
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer();

    @Override
    default public boolean matches(SmithingRecipeInput arg, World arg2) {
        return Ingredient.matches(this.template(), arg.template()) && this.base().test(arg.base()) && Ingredient.matches(this.addition(), arg.addition());
    }

    public Optional<Ingredient> template();

    public Ingredient base();

    public Optional<Ingredient> addition();

    @Override
    default public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.SMITHING;
    }
}

