package net.fabricmc.fabric.impl.resource;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;

// Used to inject into the ResourceReloader store.
public record SetupMarkerResourceReloader(
		ReloadableServerResources dataPackContents,
		HolderLookup.Provider registries,
		FeatureFlagSet featureSet
) implements ResourceManagerReloadListener {
	@Override
	public void prepareSharedState(SharedState store) {
		store.set(DataResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY, this.registries);
		store.set(DataResourceLoader.RELOADER_FEATURE_SET_KEY, this.featureSet);
		store.set(DataResourceLoader.ADVANCEMENT_LOADER_KEY, this.dataPackContents.getAdvancements());
		store.set(DataResourceLoader.RECIPE_MANAGER_KEY, this.dataPackContents.getRecipeManager());
		store.set(
				DataResourceLoader.DATA_RESOURCE_STORE_KEY,
				((FabricDataResourceStoreHolder) this.dataPackContents).fabric$getDataResourceStore()
		);
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		// Do nothing.
	}
}
