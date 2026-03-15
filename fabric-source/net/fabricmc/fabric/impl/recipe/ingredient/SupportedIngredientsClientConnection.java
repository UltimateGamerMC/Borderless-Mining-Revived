package net.fabricmc.fabric.impl.recipe.ingredient;

import java.util.Set;

import net.minecraft.network.Connection;
import net.minecraft.resources.Identifier;

/**
 * Implemented on {@link Connection} to store which custom ingredients the client supports.
 */
public interface SupportedIngredientsClientConnection {
	void fabric_setSupportedCustomIngredients(Set<Identifier> supportedCustomIngredients);

	Set<Identifier> fabric_getSupportedCustomIngredients();
}
