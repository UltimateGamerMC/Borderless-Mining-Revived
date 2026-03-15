/*
 * Internal private/static methods:
 *   Lnet/minecraft/world/entity/EntityTrackingStatus;method_36747()[Lnet/minecraft/world/entity/EntityTrackingStatus;
 */
package net.minecraft.world.entity;

import net.minecraft.server.world.ChunkLevelType;

public enum EntityTrackingStatus {
    HIDDEN(false, false),
    TRACKED(true, false),
    TICKING(true, true);

    private final boolean tracked;
    private final boolean tick;

    private EntityTrackingStatus(boolean tracked, boolean tick) {
        this.tracked = tracked;
        this.tick = tick;
    }

    public boolean shouldTick() {
        return this.tick;
    }

    public boolean shouldTrack() {
        return this.tracked;
    }

    public static EntityTrackingStatus fromLevelType(ChunkLevelType levelType) {
        if (levelType.isAfter(ChunkLevelType.ENTITY_TICKING)) {
            return TICKING;
        }
        if (levelType.isAfter(ChunkLevelType.FULL)) {
            return TRACKED;
        }
        return HIDDEN;
    }
}

