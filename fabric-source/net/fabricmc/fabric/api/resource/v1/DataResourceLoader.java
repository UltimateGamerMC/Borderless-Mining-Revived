package net.fabricmc.fabric.api.resource.v1;

import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;

import net.fabricmc.fabric.impl.resource.DataResourceLoaderImpl;

/**
 * Provides various hooks into the {@linkplain net.minecraft.server.packs.PackType#SERVER_DATA server data} resource loader.
 */
@ApiStatus.NonExtendable
public interface DataResourceLoader extends ResourceLoader {
	/**
	 * The resource reloader store key for the recipe manager.
	 *
	 * @apiNote The recipe manager is only available in {@linkplain PackType#SERVER_DATA server data} resource reloaders.
	 * <br/>
	 * It should <b>only</b> be accessed in the application phase of the resource reloader,
	 * and you should depend on {@link net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys.Server#RECIPES}.
	 */
	PreparableReloadListener.StateKey<RecipeManager> RECIPE_MANAGER_KEY = new PreparableReloadListener.StateKey<>();
	/**
	 * The resource reloader store key for the advancement loader.
	 *
	 * @apiNote The advancement loader is only available in {@linkplain PackType#SERVER_DATA server data} resource reloaders.
	 * <br/>
	 * It should <b>only</b> be accessed in the application phase of the resource reloader,
	 * and you should depend on {@link net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys.Server#ADVANCEMENTS}.
	 */
	PreparableReloadListener.StateKey<ServerAdvancementManager> ADVANCEMENT_LOADER_KEY = new PreparableReloadListener.StateKey<>();
	/**
	 * The resource reloader store key for the data resource store.
	 *
	 * @apiNote The data resource store is only available in {@linkplain PackType#SERVER_DATA server data} resource reloaders.
	 * <br/>
	 * It should <b>only</b> be mutated in the application phase of the resource reloader.
	 */
	PreparableReloadListener.StateKey<DataResourceStore.Mutable> DATA_RESOURCE_STORE_KEY = new PreparableReloadListener.StateKey<>();

	static DataResourceLoader get() {
		return DataResourceLoaderImpl.INSTANCE;
	}

	/**
	 * Registers a data resource reloader.
	 *
	 * @param id the identifier of the resource reloader
	 * @param factory the factory function of the resource reloader
	 * @see #registerReloader(Identifier, PreparableReloadListener)
	 * @see #addReloaderOrdering(Identifier, Identifier)
	 *
	 * @apiNote In most cases {@link #registerReloader(Identifier, PreparableReloadListener)} is sufficient and should be preferred,
	 * but for some resource reloaders like {@link net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener} constructing the resource reloader
	 * with a known instance of the wrapper lookup is required.
	 * <br/>
	 * While this may encourage stateful resource reloaders, it is best to primarily use resource reloaders as stateless loaders,
	 * as storing a state may easily lead to incomplete or leaking data.
	 */
	void registerReloader(
			Identifier id,
			Function<HolderLookup.Provider, PreparableReloadListener> factory
	);
}
