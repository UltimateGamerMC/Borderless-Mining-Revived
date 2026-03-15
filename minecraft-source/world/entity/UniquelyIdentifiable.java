package net.minecraft.world.entity;

import java.util.UUID;

public interface UniquelyIdentifiable {
    public UUID getUuid();

    public boolean isRemoved();
}

