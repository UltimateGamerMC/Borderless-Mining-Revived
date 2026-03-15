package net.fabricmc.fabric.mixin.recipe;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.crafting.RecipeAccess;

import net.fabricmc.fabric.api.recipe.v1.FabricRecipeManager;

@Mixin(RecipeAccess.class)
public interface RecipeAccessMixin extends FabricRecipeManager {
}
