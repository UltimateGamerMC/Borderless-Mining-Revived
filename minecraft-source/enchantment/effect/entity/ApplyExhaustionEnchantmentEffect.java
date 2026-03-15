/*
 * External method calls:
 *   Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V
 */
package net.minecraft.enchantment.effect.entity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record ApplyExhaustionEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentEntityEffect
{
    public static final MapCodec<ApplyExhaustionEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)EnchantmentLevelBasedValue.CODEC.fieldOf("amount")).forGetter(ApplyExhaustionEnchantmentEffect::amount)).apply((Applicative<ApplyExhaustionEnchantmentEffect, ?>)instance, ApplyExhaustionEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof PlayerEntity) {
            PlayerEntity lv = (PlayerEntity)user;
            lv.addExhaustion(this.amount.getValue(level));
        }
    }

    public MapCodec<ApplyExhaustionEnchantmentEffect> getCodec() {
        return CODEC;
    }
}

