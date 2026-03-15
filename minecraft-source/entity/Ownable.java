package net.minecraft.entity;

import net.minecraft.entity.Entity;
import org.jspecify.annotations.Nullable;

public interface Ownable {
    public @Nullable Entity getOwner();
}

