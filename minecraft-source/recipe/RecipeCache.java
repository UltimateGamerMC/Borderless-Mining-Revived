/*
 * External method calls:
 *   Lnet/minecraft/recipe/RecipeCache$CachedRecipe;matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;)Z
 *   Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;
 *   Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/RecipeCache;validateRecipeManager(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/recipe/RecipeCache;sendToFront(I)V
 *   Lnet/minecraft/recipe/RecipeCache;cache(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/recipe/RecipeEntry;)V
 */
package net.minecraft.recipe;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import org.jspecify.annotations.Nullable;

public class RecipeCache {
    private final @Nullable CachedRecipe[] cache;
    private WeakReference<@Nullable ServerRecipeManager> recipeManagerRef = new WeakReference<Object>(null);

    public RecipeCache(int size) {
        this.cache = new CachedRecipe[size];
    }

    public Optional<RecipeEntry<CraftingRecipe>> getRecipe(ServerWorld world, CraftingRecipeInput input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }
        this.validateRecipeManager(world);
        for (int i = 0; i < this.cache.length; ++i) {
            CachedRecipe lv = this.cache[i];
            if (lv == null || !lv.matches(input)) continue;
            this.sendToFront(i);
            return Optional.ofNullable(lv.value());
        }
        return this.getAndCacheRecipe(input, world);
    }

    private void validateRecipeManager(ServerWorld world) {
        ServerRecipeManager lv = world.getRecipeManager();
        if (lv != this.recipeManagerRef.get()) {
            this.recipeManagerRef = new WeakReference<ServerRecipeManager>(lv);
            Arrays.fill(this.cache, null);
        }
    }

    private Optional<RecipeEntry<CraftingRecipe>> getAndCacheRecipe(CraftingRecipeInput input, ServerWorld world) {
        Optional<RecipeEntry<CraftingRecipe>> optional = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, input, world);
        this.cache(input, optional.orElse(null));
        return optional;
    }

    private void sendToFront(int index) {
        if (index > 0) {
            CachedRecipe lv = this.cache[index];
            System.arraycopy(this.cache, 0, this.cache, 1, index);
            this.cache[0] = lv;
        }
    }

    private void cache(CraftingRecipeInput input, @Nullable RecipeEntry<CraftingRecipe> recipe) {
        DefaultedList<ItemStack> lv = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            lv.set(i, input.getStackInSlot(i).copyWithCount(1));
        }
        System.arraycopy(this.cache, 0, this.cache, 1, this.cache.length - 1);
        this.cache[0] = new CachedRecipe(lv, input.getWidth(), input.getHeight(), recipe);
    }

    record CachedRecipe(DefaultedList<ItemStack> key, int width, int height, @Nullable RecipeEntry<CraftingRecipe> value) {
        public boolean matches(CraftingRecipeInput input) {
            if (this.width != input.getWidth() || this.height != input.getHeight()) {
                return false;
            }
            for (int i = 0; i < this.key.size(); ++i) {
                if (ItemStack.areItemsAndComponentsEqual(this.key.get(i), input.getStackInSlot(i))) continue;
                return false;
            }
            return true;
        }
    }
}

