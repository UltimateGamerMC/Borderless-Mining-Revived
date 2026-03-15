package net.fabricmc.fabric.impl.event.lifecycle;

import net.minecraft.server.level.FullChunkStatus;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;

/**
 * A marker interface that tracks the current {@link ServerChunkEvents#CHUNK_LEVEL_TYPE_CHANGE} level type.
 */
public interface ChunkLevelTypeEventTracker {
	void fabric_setCurrentEventLevelType(FullChunkStatus levelType);

	FullChunkStatus fabric_getCurrentEventLevelType();
}
