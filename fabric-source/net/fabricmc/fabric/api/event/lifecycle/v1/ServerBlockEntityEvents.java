package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ServerBlockEntityEvents {
	private ServerBlockEntityEvents() {
	}

	/**
	 * Called when an BlockEntity is loaded into a ServerWorld.
	 *
	 * <p>When this is event is called, the block entity is already in the world.
	 * However, its data might not be loaded yet, so don't rely on it.
	 */
	public static final Event<ServerBlockEntityEvents.Load> BLOCK_ENTITY_LOAD = EventFactory.createArrayBacked(ServerBlockEntityEvents.Load.class, callbacks -> (blockEntity, world) -> {
		for (Load callback : callbacks) {
			callback.onLoad(blockEntity, world);
		}
	});

	/**
	 * Called when an BlockEntity is about to be unloaded from a ServerWorld.
	 *
	 * <p>When this event is called, the block entity is still present on the world.
	 */
	public static final Event<Unload> BLOCK_ENTITY_UNLOAD = EventFactory.createArrayBacked(ServerBlockEntityEvents.Unload.class, callbacks -> (blockEntity, world) -> {
		for (Unload callback : callbacks) {
			callback.onUnload(blockEntity, world);
		}
	});

	@FunctionalInterface
	public interface Load {
		void onLoad(BlockEntity blockEntity, ServerLevel world);
	}

	@FunctionalInterface
	public interface Unload {
		void onUnload(BlockEntity blockEntity, ServerLevel world);
	}
}
