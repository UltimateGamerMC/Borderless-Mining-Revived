package net.minecraft.entity;

import net.minecraft.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public interface Targeter {
    public @Nullable LivingEntity getTarget();
}

