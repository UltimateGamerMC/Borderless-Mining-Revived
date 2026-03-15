/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ExplosiveProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V
 *   Lnet/minecraft/entity/damage/DamageSources;witherSkull(Lnet/minecraft/entity/projectile/WitherSkullEntity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/LivingEntity;heal(F)V
 *   Lnet/minecraft/entity/damage/DamageSources;magic()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/projectile/ExplosiveProjectileEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V
 *   Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V
 *   Lnet/minecraft/entity/projectile/ExplosiveProjectileEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/entity/projectile/ExplosiveProjectileEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 */
package net.minecraft.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class WitherSkullEntity
extends ExplosiveProjectileEntity {
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(WitherSkullEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final boolean DEFAULT_DANGEROUS = false;

    public WitherSkullEntity(EntityType<? extends WitherSkullEntity> arg, World arg2) {
        super((EntityType<? extends ExplosiveProjectileEntity>)arg, arg2);
    }

    public WitherSkullEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(EntityType.WITHER_SKULL, owner, velocity, world);
    }

    @Override
    protected float getDrag() {
        return this.isCharged() ? 0.73f : super.getDrag();
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        if (this.isCharged() && WitherEntity.canDestroy(blockState)) {
            return Math.min(0.8f, max);
        }
        return max;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        boolean bl;
        LivingEntity lv4;
        super.onEntityHit(entityHitResult);
        World world = this.getEntityWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv = (ServerWorld)world;
        Entity lv2 = entityHitResult.getEntity();
        Entity lv3 = this.getOwner();
        if (lv3 instanceof LivingEntity) {
            lv4 = (LivingEntity)lv3;
            DamageSource lv5 = this.getDamageSources().witherSkull(this, lv4);
            bl = lv2.damage(lv, lv5, 8.0f);
            if (bl) {
                if (lv2.isAlive()) {
                    EnchantmentHelper.onTargetDamaged(lv, lv2, lv5);
                } else {
                    lv4.heal(5.0f);
                }
            }
        } else {
            bl = lv2.damage(lv, this.getDamageSources().magic(), 5.0f);
        }
        if (bl && lv2 instanceof LivingEntity) {
            lv4 = (LivingEntity)lv2;
            int i = 0;
            if (this.getEntityWorld().getDifficulty() == Difficulty.NORMAL) {
                i = 10;
            } else if (this.getEntityWorld().getDifficulty() == Difficulty.HARD) {
                i = 40;
            }
            if (i > 0) {
                lv4.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20 * i, 1), this.getEffectCause());
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getEntityWorld().isClient()) {
            this.getEntityWorld().createExplosion((Entity)this, this.getX(), this.getY(), this.getZ(), 1.0f, false, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(CHARGED, false);
    }

    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.dataTracker.set(CHARGED, charged);
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("dangerous", this.isCharged());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setCharged(view.getBoolean("dangerous", false));
    }
}

