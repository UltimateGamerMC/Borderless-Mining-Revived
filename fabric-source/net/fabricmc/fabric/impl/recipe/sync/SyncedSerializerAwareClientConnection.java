package net.fabricmc.fabric.impl.recipe.sync;

import java.util.Set;

import net.minecraft.network.Connection;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Implemented on {@link Connection} to store which recipe serializers should be synced to client.
 */
public interface SyncedSerializerAwareClientConnection {
	void fabric_setSyncedRecipeSerializers(Set<RecipeSerializer<?>> syncedRecipeSerializers);

	Set<RecipeSerializer<?>> fabric_getSyncedRecipeSerializers();
}
