/*
 * Internal private/static methods:
 *   Lnet/minecraft/world/border/WorldBorderStage;method_36740()[Lnet/minecraft/world/border/WorldBorderStage;
 */
package net.minecraft.world.border;

public enum WorldBorderStage {
    GROWING(4259712),
    SHRINKING(0xFF3030),
    STATIONARY(2138367);

    private final int color;

    private WorldBorderStage(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}

