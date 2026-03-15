package net.fabricmc.fabric.mixin.resource.conditions;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.data.DataProvider;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

/**
 * Make the {@value ResourceConditions#CONDITIONS_KEY} appear first in generated JSON objects.
 */
@Mixin(DataProvider.class)
public interface DataProviderMixin {
	@Dynamic("lambda method passed to Util.make")
	@Inject(method = "method_43808", at = @At("HEAD"))
	private static void fabric_injectResourceConditionsSortOrder(Object2IntOpenHashMap<String> map, CallbackInfo ci) {
		map.put(ResourceConditions.CONDITIONS_KEY, -100);
	}
}
