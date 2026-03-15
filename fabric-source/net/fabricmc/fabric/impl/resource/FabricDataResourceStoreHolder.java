package net.fabricmc.fabric.impl.resource;

import net.fabricmc.fabric.api.resource.v1.DataResourceStore;

public interface FabricDataResourceStoreHolder {
	DataResourceStore.Mutable fabric$getDataResourceStore();
}
