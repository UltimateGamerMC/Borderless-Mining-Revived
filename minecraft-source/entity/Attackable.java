package net.minecraft.entity;

import net.minecraft.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public interface Attackable {
    public @Nullable LivingEntity getLastAttacker();
}

