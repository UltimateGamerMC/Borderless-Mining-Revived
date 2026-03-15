package net.fabricmc.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.Registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistry;

@Mixin(Registry.class)
public interface RegistryMixin extends FabricRegistry {
}
