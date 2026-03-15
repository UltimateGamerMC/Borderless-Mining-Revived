/*
 * Internal private/static methods:
 *   Lnet/minecraft/server/world/ChunkLevelType;method_36576()[Lnet/minecraft/server/world/ChunkLevelType;
 */
package net.minecraft.server.world;

public enum ChunkLevelType {
    INACCESSIBLE,
    FULL,
    BLOCK_TICKING,
    ENTITY_TICKING;


    public boolean isAfter(ChunkLevelType levelType) {
        return this.ordinal() >= levelType.ordinal();
    }
}

