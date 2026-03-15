package net.fabricmc.fabric.api.datagen.v1.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;

/**
 * Injected to all {@link RecipeOutput} instances.
 */
public interface FabricRecipeExporter {
	/**
	 * Override this method to change the recipe identifier.
	 *
	 * <p>The default implementation returns the ID unchanged.
	 * Fabric API implementations automatically apply the corresponding method in
	 * {@link net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider FabricRecipeProvider}.
	 *
	 * @see net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider#getRecipeIdentifier(Identifier)
	 */
	default Identifier getRecipeIdentifier(Identifier recipeId) {
		return recipeId;
	}
}
