/*
 * External method calls:
 *   Lnet/minecraft/entity/LivingEntity;heal(F)V
 *   Lnet/minecraft/entity/damage/DamageSources;magic()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/entity/damage/DamageSources;indirectMagic(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;
 */
package net.minecraft.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import org.jspecify.annotations.Nullable;

class InstantHealthOrDamageStatusEffect
extends InstantStatusEffect {
    private final boolean damage;

    public InstantHealthOrDamageStatusEffect(StatusEffectCategory category, int color, boolean damage) {
        super(category, color);
        this.damage = damage;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (this.damage == entity.hasInvertedHealingAndHarm()) {
            entity.heal(Math.max(4 << amplifier, 0));
        } else {
            entity.damage(world, entity.getDamageSources().magic(), 6 << amplifier);
        }
        return true;
    }

    @Override
    public void applyInstantEffect(ServerWorld world, @Nullable Entity effectEntity, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        if (this.damage == target.hasInvertedHealingAndHarm()) {
            int j = (int)(proximity * (double)(4 << amplifier) + 0.5);
            target.heal(j);
        } else {
            int j = (int)(proximity * (double)(6 << amplifier) + 0.5);
            if (effectEntity == null) {
                target.damage(world, target.getDamageSources().magic(), j);
            } else {
                target.damage(world, target.getDamageSources().indirectMagic(effectEntity, attacker), j);
            }
        }
    }
}

