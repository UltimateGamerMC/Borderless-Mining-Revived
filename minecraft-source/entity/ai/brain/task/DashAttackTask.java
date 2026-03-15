/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AnimalEntity;playSoundIfNotSilent(Lnet/minecraft/sound/SoundEvent;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;lookAtEntity(Lnet/minecraft/entity/Entity;FF)V
 *   Lnet/minecraft/util/TypeFilter;instanceOf(Ljava/lang/Class;)Lnet/minecraft/util/TypeFilter;
 *   Lnet/minecraft/server/world/ServerWorld;collectEntitiesByType(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;Ljava/util/List;I)V
 *   Lnet/minecraft/entity/damage/DamageSources;mobAttack(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;knockbackTarget(Lnet/minecraft/entity/Entity;FLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/TargetPredicate;test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/DashAttackTask;attack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/ai/brain/task/DashAttackTask;knockbackTarget(Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/ai/brain/task/DashAttackTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/DashAttackTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/DashAttackTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DashAttackTask
extends MultiTickTask<AnimalEntity> {
    private final int cooldownTicks;
    private final TargetPredicate predicate;
    private final float speed;
    private final float knockbackStrength;
    private final double maxDistance;
    private final double maxEntitySpeed;
    private final SoundEvent sound;
    private Vec3d velocity;
    private Vec3d lastPos;

    public DashAttackTask(int cooldownTicks, TargetPredicate predicate, float speed, float knockbackStrength, double maxEntitySpeed, double maxDistance, SoundEvent sound) {
        super(ImmutableMap.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));
        this.cooldownTicks = cooldownTicks;
        this.predicate = predicate;
        this.speed = speed;
        this.knockbackStrength = knockbackStrength;
        this.maxEntitySpeed = maxEntitySpeed;
        this.maxDistance = maxDistance;
        this.sound = sound;
        this.velocity = Vec3d.ZERO;
        this.lastPos = Vec3d.ZERO;
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, AnimalEntity arg2) {
        return arg2.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, AnimalEntity arg2, long l) {
        TameableEntity lv3;
        Brain<Integer> lv = arg2.getBrain();
        Optional<LivingEntity> optional = lv.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET);
        if (optional.isEmpty()) {
            return false;
        }
        LivingEntity lv2 = optional.get();
        if (arg2 instanceof TameableEntity && (lv3 = (TameableEntity)arg2).isTamed()) {
            return false;
        }
        if (arg2.getEntityPos().subtract(this.lastPos).lengthSquared() >= this.maxEntitySpeed * this.maxEntitySpeed) {
            return false;
        }
        if (lv2.getEntityPos().subtract(arg2.getEntityPos()).lengthSquared() >= this.maxDistance * this.maxDistance) {
            return false;
        }
        if (!arg2.canSee(lv2)) {
            return false;
        }
        return !lv.hasMemoryModule(MemoryModuleType.CHARGE_COOLDOWN_TICKS);
    }

    @Override
    protected void run(ServerWorld arg, AnimalEntity arg2, long l) {
        Brain<?> lv = arg2.getBrain();
        this.lastPos = arg2.getEntityPos();
        LivingEntity lv2 = lv.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get();
        Vec3d lv3 = lv2.getEntityPos().subtract(arg2.getEntityPos()).normalize();
        this.velocity = lv3.multiply(this.speed);
        if (this.shouldKeepRunning(arg, arg2, l)) {
            arg2.playSoundIfNotSilent(this.sound);
        }
    }

    @Override
    protected void keepRunning(ServerWorld arg, AnimalEntity arg2, long l) {
        Brain<?> lv = arg2.getBrain();
        LivingEntity lv2 = lv.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        arg2.lookAtEntity(lv2, 360.0f, 360.0f);
        arg2.setVelocity(this.velocity);
        ArrayList list = new ArrayList(1);
        arg.collectEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), arg2.getBoundingBox(), target -> this.predicate.test(arg, arg2, (LivingEntity)target), list, 1);
        if (!list.isEmpty()) {
            LivingEntity lv3 = (LivingEntity)list.get(0);
            if (arg2.hasPassenger(lv3)) {
                return;
            }
            this.attack(arg, arg2, lv3);
            this.knockbackTarget(arg2, lv3);
            this.finishRunning(arg, arg2, l);
        }
    }

    private void attack(ServerWorld world, AnimalEntity entity, LivingEntity target) {
        float f;
        DamageSource lv = world.getDamageSources().mobAttack(entity);
        if (target.damage(world, lv, f = (float)entity.getAttributeValue(EntityAttributes.ATTACK_DAMAGE))) {
            EnchantmentHelper.onTargetDamaged(world, target, lv);
        }
    }

    private void knockbackTarget(AnimalEntity entity, LivingEntity target) {
        int i = entity.hasStatusEffect(StatusEffects.SPEED) ? entity.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1 : 0;
        int j = entity.hasStatusEffect(StatusEffects.SLOWNESS) ? entity.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier() + 1 : 0;
        float f = 0.25f * (float)(i - j);
        float g = MathHelper.clamp(this.speed * (float)entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED), 0.2f, 2.0f) + f;
        entity.knockbackTarget(target, g * this.knockbackStrength, entity.getVelocity());
    }

    @Override
    protected void finishRunning(ServerWorld arg, AnimalEntity arg2, long l) {
        arg2.getBrain().remember(MemoryModuleType.CHARGE_COOLDOWN_TICKS, this.cooldownTicks);
        arg2.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected /* synthetic */ void finishRunning(ServerWorld world, LivingEntity entity, long time) {
        this.finishRunning(world, (AnimalEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void run(ServerWorld world, LivingEntity entity, long time) {
        this.run(world, (AnimalEntity)entity, time);
    }
}

