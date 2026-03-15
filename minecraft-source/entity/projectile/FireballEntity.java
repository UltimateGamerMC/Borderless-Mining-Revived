/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/AbstractFireballEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V
 *   Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V
 *   Lnet/minecraft/entity/projectile/AbstractFireballEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V
 *   Lnet/minecraft/entity/damage/DamageSources;fireball(Lnet/minecraft/entity/projectile/AbstractFireballEntity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/projectile/AbstractFireballEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putByte(Ljava/lang/String;B)V
 *   Lnet/minecraft/entity/projectile/AbstractFireballEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 */
package net.minecraft.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.rule.GameRules;

public class FireballEntity
extends AbstractFireballEntity {
    private static final byte DEFAULT_EXPLOSION_POWER = 1;
    private int explosionPower = 1;

    public FireballEntity(EntityType<? extends FireballEntity> arg, World arg2) {
        super((EntityType<? extends AbstractFireballEntity>)arg, arg2);
    }

    public FireballEntity(World world, LivingEntity owner, Vec3d velocity, int explosionPower) {
        super((EntityType<? extends AbstractFireballEntity>)EntityType.FIREBALL, owner, velocity, world);
        this.explosionPower = explosionPower;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        World world = this.getEntityWorld();
        if (world instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            boolean bl = lv.getGameRules().getValue(GameRules.DO_MOB_GRIEFING);
            this.getEntityWorld().createExplosion((Entity)this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, bl, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        World world = this.getEntityWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv = (ServerWorld)world;
        Entity lv2 = entityHitResult.getEntity();
        Entity lv3 = this.getOwner();
        DamageSource lv4 = this.getDamageSources().fireball(this, lv3);
        lv2.damage(lv, lv4, 6.0f);
        EnchantmentHelper.onTargetDamaged(lv, lv2, lv4);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.explosionPower = view.getByte("ExplosionPower", (byte)1);
    }
}

