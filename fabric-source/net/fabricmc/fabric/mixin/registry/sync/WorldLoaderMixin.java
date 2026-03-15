package net.fabricmc.fabric.mixin.registry.sync;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.WorldLoader;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

// Implements dynamic registry loading.
@Mixin(WorldLoader.class)
abstract class WorldLoaderMixin {
	@ModifyArg(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;load(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;", ordinal = 0), index = 2, allow = 1)
	private static List<RegistryDataLoader.RegistryData<?>> modifyLoadedEntries(List<RegistryDataLoader.RegistryData<?>> entries) {
		return DynamicRegistries.getDynamicRegistries();
	}
}
