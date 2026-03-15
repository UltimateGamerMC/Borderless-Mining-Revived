package net.fabricmc.fabric.api.resource.v1.reloader;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.resources.PreparableReloadListener;

/**
 * A variant of {@link net.minecraft.server.packs.resources.SimplePreparableReloadListener}
 * which passes the shared state store instead of the resource manager in its methods.
 *
 * <p>In essence, there are two stages:
 *
 * <ul><li>prepare: create an instance of your data object containing all loaded and
 * processed information,
 * <li>apply: apply the information from the data object to the game instance.</ul>
 *
 * <p>The prepare stage should be self-contained as it can run on any thread! However,
 * the apply stage is guaranteed to run on the game thread.
 *
 * <p>For a fully synchronous alternative, consider using
 * {@link net.minecraft.server.packs.resources.ResourceManagerReloadListener}.
 *
 * @param <T> the data object
 */
public abstract class SimpleResourceReloader<T> implements PreparableReloadListener {
	public final CompletableFuture<Void> reload(SharedState store, Executor prepareExecutor, PreparationBarrier reloadSynchronizer, Executor applyExecutor) {
		CompletableFuture<T> prepareStep = CompletableFuture.supplyAsync(() -> this.prepare(store), prepareExecutor);
		Objects.requireNonNull(reloadSynchronizer);
		return prepareStep.thenCompose(reloadSynchronizer::wait)
				.thenAcceptAsync((prepared) -> this.apply(prepared, store), applyExecutor);
	}

	/**
	 * Asynchronously processes and prepares resource-based data.
	 * The code must be thread-safe and not modify game state!
	 *
	 * @param store the data store used for sharing state between resource reloaders
	 * @return the prepared data
	 */
	protected abstract T prepare(SharedState store);

	/**
	 * Synchronously applies prepared data to the game state.
	 *
	 * @param prepared the prepared data
	 * @param store the data store used for sharing state between resource reloaders
	 */
	protected abstract void apply(T prepared, SharedState store);
}
