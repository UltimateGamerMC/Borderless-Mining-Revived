package net.minecraft.world.entity;

import java.util.UUID;
import net.minecraft.world.entity.UniquelyIdentifiable;
import org.jspecify.annotations.Nullable;

public interface EntityQueriable<IdentifiedType extends UniquelyIdentifiable> {
    public @Nullable IdentifiedType lookup(UUID var1);
}

