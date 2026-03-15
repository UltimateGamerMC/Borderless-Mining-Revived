/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/SpiderEntity;createSpiderAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/mob/SpiderEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z
 */
package net.minecraft.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class CaveSpiderEntity
extends SpiderEntity {
    public CaveSpiderEntity(EntityType<? extends CaveSpiderEntity> arg, World arg2) {
        super((EntityType<? extends SpiderEntity>)arg, arg2);
    }

    public static DefaultAttributeContainer.Builder createCaveSpiderAttributes() {
        return SpiderEntity.createSpiderAttributes().add(EntityAttributes.MAX_HEALTH, 12.0);
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (super.tryAttack(world, target)) {
            if (target instanceof LivingEntity) {
                int i = 0;
                if (this.getEntityWorld().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.getEntityWorld().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }
                if (i > 0) {
                    ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, i * 20, 0), this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        return entityData;
    }

    @Override
    public Vec3d getVehicleAttachmentPos(Entity vehicle) {
        if (vehicle.getWidth() <= this.getWidth()) {
            return new Vec3d(0.0, 0.21875 * (double)this.getScale(), 0.0);
        }
        return super.getVehicleAttachmentPos(vehicle);
    }
}

