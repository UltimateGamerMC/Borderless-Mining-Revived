package net.fabricmc.fabric.mixin.datagen;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.data.DataProvider;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.impl.recipe.ingredient.CustomIngredientImpl;

@Mixin(DataProvider.class)
public interface DataProviderMixin {
	/**
	 * Adjust the default sort order of some keys provided by Fabric API.
	 */
	@Inject(method = "method_43808", at = @At("RETURN"))
	private static void addFabricKeySortOrders(Object2IntOpenHashMap<String> map, CallbackInfo ci) {
		map.put(ResourceConditions.CONDITIONS_KEY, -100); // always at the beginning
		map.put(CustomIngredientImpl.TYPE_KEY, 0); // mimic vanilla "type"
	}
}
