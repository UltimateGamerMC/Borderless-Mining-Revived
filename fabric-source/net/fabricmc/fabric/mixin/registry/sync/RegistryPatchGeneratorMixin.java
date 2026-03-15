package net.fabricmc.fabric.mixin.registry.sync;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.RegistryDataLoader;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

@Mixin(RegistryPatchGenerator.class)
class RegistryPatchGeneratorMixin {
	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/resources/RegistryDataLoader;WORLDGEN_REGISTRIES:Ljava/util/List;"), method = "method_54839")
	private static List<RegistryDataLoader.RegistryData<?>> getDynamicRegistries() {
		// Register cloners for all dynamic registries.
		return DynamicRegistries.getDynamicRegistries();
	}
}
