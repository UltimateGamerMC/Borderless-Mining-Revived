package net.fabricmc.fabric.api.recipe.v1;

import net.minecraft.world.item.crafting.RecipeAccess;

import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.fabricmc.fabric.impl.recipe.sync.SynchronizedRecipesImpl;

/**
 * General-purpose Fabric-provided extensions for {@link RecipeAccess} class.
 */
public interface FabricRecipeManager {
	default SynchronizedRecipes getSynchronizedRecipes() {
		// Fallback implementation in case someone implements RecipeManager interface on a custom class.
		return SynchronizedRecipesImpl.EMPTY;
	}
}
