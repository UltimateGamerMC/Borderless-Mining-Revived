/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/PathAwareEntity;squaredDistanceTo(DDD)D
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;findFrom(Lnet/minecraft/entity/mob/PathAwareEntity;DDILnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/task/MultiTickTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(DDDD)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/SpearFleeTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearFleeTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearFleeTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Map;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.SpearChargeTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class SpearFleeTask
extends MultiTickTask<PathAwareEntity> {
    public static final int field_64633 = 9;
    public static final int field_64634 = 11;
    public static final int RUN_TIME = 100;
    double speed;

    public SpearFleeTask(double speedFactor) {
        super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryModuleState.VALUE_PRESENT), 100);
        this.speed = speedFactor;
    }

    private @Nullable LivingEntity getAttackTarget(PathAwareEntity entity) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private boolean shouldAttack(PathAwareEntity entity) {
        return this.getAttackTarget(entity) != null && entity.getMainHandStack().contains(DataComponentTypes.KINETIC_WEAPON);
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, PathAwareEntity arg2) {
        double e;
        if (!this.shouldAttack(arg2) || arg2.isUsingItem()) {
            return false;
        }
        if (arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_STATUS).orElse(SpearChargeTask.AdvanceState.APPROACH) != SpearChargeTask.AdvanceState.RETREAT) {
            return false;
        }
        LivingEntity lv = this.getAttackTarget(arg2);
        double d = arg2.squaredDistanceTo(lv.getX(), lv.getY(), lv.getZ());
        int i = arg2.hasVehicle() ? 2 : 0;
        Vec3d lv2 = FuzzyTargeting.findFrom(arg2, Math.max(0.0, (double)(9 + i) - (e = Math.sqrt(d))), Math.max(1.0, (double)(11 + i) - e), 7, lv.getEntityPos());
        if (lv2 == null) {
            return false;
        }
        arg2.getBrain().remember(MemoryModuleType.SPEAR_FLEEING_POSITION, lv2);
        return true;
    }

    @Override
    protected void run(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.setAttacking(true);
        arg2.getBrain().remember(MemoryModuleType.SPEAR_FLEEING_TIME, 0);
        super.run(arg, arg2, l);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        return arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse(100) < 100 && arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).isPresent() && !arg2.getNavigation().isIdle() && this.shouldAttack(arg2);
    }

    @Override
    protected void keepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        float f;
        LivingEntity lv = this.getAttackTarget(arg2);
        Entity lv2 = arg2.getRootVehicle();
        if (lv2 instanceof MobEntity) {
            MobEntity lv3 = (MobEntity)lv2;
            f = lv3.getRiderChargingSpeedMultiplier();
        } else {
            f = 1.0f;
        }
        float f2 = f;
        arg2.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(lv, true));
        arg2.getBrain().remember(MemoryModuleType.SPEAR_FLEEING_TIME, arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse(0) + 1);
        arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).ifPresent(pos -> arg2.getNavigation().startMovingTo(pos.x, pos.y, pos.z, (double)f2 * this.speed));
    }

    @Override
    protected void finishRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.getNavigation().stop();
        arg2.setAttacking(false);
        arg2.clearActiveItem();
        arg2.getBrain().forget(MemoryModuleType.SPEAR_FLEEING_TIME);
        arg2.getBrain().forget(MemoryModuleType.SPEAR_FLEEING_POSITION);
        arg2.getBrain().forget(MemoryModuleType.SPEAR_STATUS);
    }

    @Override
    protected /* synthetic */ boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return this.shouldKeepRunning(world, (PathAwareEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void finishRunning(ServerWorld world, LivingEntity entity, long time) {
        this.finishRunning(world, (PathAwareEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void run(ServerWorld world, LivingEntity entity, long time) {
        this.run(world, (PathAwareEntity)entity, time);
    }
}

