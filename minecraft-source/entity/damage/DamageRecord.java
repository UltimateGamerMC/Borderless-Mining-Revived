package net.minecraft.entity.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.FallLocation;
import org.jspecify.annotations.Nullable;

public record DamageRecord(DamageSource damageSource, float damage, @Nullable FallLocation fallLocation, float fallDistance) {
}

