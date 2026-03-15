/*
 * External method calls:
 *   Lnet/minecraft/particle/EffectParticleEffect;of(Lnet/minecraft/particle/ParticleType;IF)Lnet/minecraft/particle/EffectParticleEffect;
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putInt(Ljava/lang/String;I)V
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.EffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class SpectralArrowEntity
extends PersistentProjectileEntity {
    private static final int DEFAULT_DURATION = 200;
    private int duration = 200;

    public SpectralArrowEntity(EntityType<? extends SpectralArrowEntity> arg, World arg2) {
        super((EntityType<? extends PersistentProjectileEntity>)arg, arg2);
    }

    public SpectralArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.SPECTRAL_ARROW, owner, world, stack, shotFrom);
    }

    public SpectralArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.SPECTRAL_ARROW, x, y, z, world, stack, shotFrom);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getEntityWorld().isClient() && !this.isInGround()) {
            this.getEntityWorld().addParticleClient(EffectParticleEffect.of(ParticleTypes.EFFECT, -1, 1.0f), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        StatusEffectInstance lv = new StatusEffectInstance(StatusEffects.GLOWING, this.duration, 0);
        target.addStatusEffect(lv, this.getEffectCause());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.duration = view.getInt("Duration", 200);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("Duration", this.duration);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }
}

