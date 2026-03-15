package net.fabricmc.fabric.impl.content.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public record FuelRegistryEventsContextImpl(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, int baseSmeltTime) implements FuelRegistryEvents.Context {
}
