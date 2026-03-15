package net.fabricmc.fabric.impl.event.lifecycle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public final class LifecycleEventsImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		// Part of impl for block entity events
		ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			((LoadedChunksCache) world).fabric_markLoaded(chunk);
		});

		ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			((LoadedChunksCache) world).fabric_markUnloaded(chunk);
		});

		// Fire block entity unload events.
		// This handles the edge case where going through a portal will cause block entities to unload without warning.
		ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
				ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world);
			}
		});

		// We use the world unload event so worlds that are dynamically hot(un)loaded get (block) entity unload events fired when shut down.
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			for (LevelChunk chunk : ((LoadedChunksCache) world).fabric_getLoadedChunks()) {
				for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
					ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, world);
				}
			}

			for (Entity entity : world.getAllEntities()) {
				ServerEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, world);
			}
		});
	}
}
