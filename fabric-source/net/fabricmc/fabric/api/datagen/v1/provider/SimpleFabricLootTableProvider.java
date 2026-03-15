package net.fabricmc.fabric.api.datagen.v1.provider;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl;

/**
 * Extend this class and implement {@link #generate}. Register an instance of the class with {@link FabricDataGenerator.Pack#addProvider} in a {@link net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint}.
 */
public abstract class SimpleFabricLootTableProvider implements FabricLootTableProvider {
	protected final FabricDataOutput output;
	private final CompletableFuture<HolderLookup.Provider> registryLookup;
	protected final ContextKeySet contextType;

	public SimpleFabricLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup, ContextKeySet contextType) {
		this.output = output;
		this.registryLookup = registryLookup;
		this.contextType = contextType;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		return FabricLootTableProviderImpl.run(writer, this, contextType, output, registryLookup);
	}

	@Override
	public String getName() {
		return Objects.requireNonNull(LootContextParamSets.REGISTRY.inverse().get(contextType), "Could not get id for loot context type") + " Loot Table";
	}
}
