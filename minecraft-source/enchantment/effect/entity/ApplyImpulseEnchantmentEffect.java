/*
 * External method calls:
 *   Lnet/minecraft/entity/Entity;addVelocityInternal(Lnet/minecraft/util/math/Vec3d;)V
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

public record ApplyImpulseEnchantmentEffect(Vec3d direction, Vec3d coordinateScale, EnchantmentLevelBasedValue magnitude) implements EnchantmentEntityEffect
{
    public static final MapCodec<ApplyImpulseEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Vec3d.CODEC.fieldOf("direction")).forGetter(ApplyImpulseEnchantmentEffect::direction), ((MapCodec)Vec3d.CODEC.fieldOf("coordinate_scale")).forGetter(ApplyImpulseEnchantmentEffect::coordinateScale), ((MapCodec)EnchantmentLevelBasedValue.CODEC.fieldOf("magnitude")).forGetter(ApplyImpulseEnchantmentEffect::magnitude)).apply((Applicative<ApplyImpulseEnchantmentEffect, ?>)instance, ApplyImpulseEnchantmentEffect::new));
    private static final int CURRENT_EXPLOSION_RESET_GRACE_TIME = 10;

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        Vec3d lv = user.getRotationVector();
        Vec3d lv2 = lv.transformLocalPos(this.direction).multiply(this.coordinateScale).multiply(this.magnitude.getValue(level));
        user.addVelocityInternal(lv2);
        user.knockedBack = true;
        user.velocityDirty = true;
        if (user instanceof PlayerEntity) {
            PlayerEntity lv3 = (PlayerEntity)user;
            lv3.setCurrentExplosionResetGraceTime(10);
        }
    }

    public MapCodec<ApplyImpulseEnchantmentEffect> getCodec() {
        return CODEC;
    }
}

