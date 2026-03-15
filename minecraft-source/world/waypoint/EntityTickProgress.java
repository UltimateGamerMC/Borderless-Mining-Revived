package net.minecraft.world.waypoint;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface EntityTickProgress {
    public float getTickProgress(Entity var1);
}

