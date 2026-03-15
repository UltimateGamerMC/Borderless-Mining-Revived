package net.fabricmc.fabric.mixin.datagen.recipe;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.recipes.RecipeOutput;

import net.fabricmc.fabric.api.datagen.v1.recipe.FabricRecipeExporter;

@Mixin(RecipeOutput.class)
public interface RecipeOutputMixin extends FabricRecipeExporter {
}
