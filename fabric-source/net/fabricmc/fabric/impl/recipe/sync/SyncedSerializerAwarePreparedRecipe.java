package net.fabricmc.fabric.impl.recipe.sync;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface SyncedSerializerAwarePreparedRecipe {
	@Nullable
	List<RecipeHolder<?>> fabric_getRecipesBySyncedSerializer(RecipeSerializer<?> serializer);
}
