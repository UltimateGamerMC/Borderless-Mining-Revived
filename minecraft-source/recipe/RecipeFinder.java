/*
 * External method calls:
 *   Lnet/minecraft/entity/player/PlayerInventory;usableWhenFillingSlot(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/recipe/RecipeMatcher;match(Ljava/util/List;ILnet/minecraft/recipe/RecipeMatcher$ItemCallback;)Z
 *   Lnet/minecraft/recipe/RecipeMatcher;countCrafts(Ljava/util/List;ILnet/minecraft/recipe/RecipeMatcher$ItemCallback;)I
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/RecipeFinder;addInput(Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/recipe/RecipeFinder;addInput(Lnet/minecraft/item/ItemStack;I)V
 *   Lnet/minecraft/recipe/RecipeFinder;countCrafts(Lnet/minecraft/recipe/Recipe;ILnet/minecraft/recipe/RecipeMatcher$ItemCallback;)I
 */
package net.minecraft.recipe;

import java.util.List;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.entry.RegistryEntry;
import org.jspecify.annotations.Nullable;

public class RecipeFinder {
    private final RecipeMatcher<RegistryEntry<Item>> recipeMatcher = new RecipeMatcher();

    public void addInputIfUsable(ItemStack item) {
        if (PlayerInventory.usableWhenFillingSlot(item)) {
            this.addInput(item);
        }
    }

    public void addInput(ItemStack item) {
        this.addInput(item, item.getMaxCount());
    }

    public void addInput(ItemStack item, int maxCount) {
        if (!item.isEmpty()) {
            int j = Math.min(maxCount, item.getCount());
            this.recipeMatcher.add(item.getRegistryEntry(), j);
        }
    }

    public boolean isCraftable(Recipe<?> recipe, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        return this.isCraftable(recipe, 1, itemCallback);
    }

    public boolean isCraftable(Recipe<?> recipe, int quantity, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        IngredientPlacement lv = recipe.getIngredientPlacement();
        if (lv.hasNoPlacement()) {
            return false;
        }
        return this.isCraftable(lv.getIngredients(), quantity, itemCallback);
    }

    public boolean isCraftable(List<? extends RecipeMatcher.RawIngredient<RegistryEntry<Item>>> rawIngredients, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        return this.isCraftable(rawIngredients, 1, itemCallback);
    }

    private boolean isCraftable(List<? extends RecipeMatcher.RawIngredient<RegistryEntry<Item>>> rawIngredients, int quantity, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        return this.recipeMatcher.match(rawIngredients, quantity, itemCallback);
    }

    public int countCrafts(Recipe<?> recipe, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        return this.countCrafts(recipe, Integer.MAX_VALUE, itemCallback);
    }

    public int countCrafts(Recipe<?> recipe, int max, @Nullable RecipeMatcher.ItemCallback<RegistryEntry<Item>> itemCallback) {
        return this.recipeMatcher.countCrafts(recipe.getIngredientPlacement().getIngredients(), max, itemCallback);
    }

    public void clear() {
        this.recipeMatcher.clear();
    }
}

