/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/task/MultiTickTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V
 *   Lnet/minecraft/entity/mob/PathAwareEntity;squaredDistanceTo(DDD)D
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(Lnet/minecraft/entity/Entity;D)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/SpearAttackTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearAttackTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearAttackTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Map;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.SpearChargeTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import org.jspecify.annotations.Nullable;

public class SpearAttackTask
extends MultiTickTask<PathAwareEntity> {
    double speed;
    float squaredAttackRange;

    public SpearAttackTask(double speed, float attackRange) {
        super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryModuleState.VALUE_ABSENT));
        this.speed = speed;
        this.squaredAttackRange = attackRange * attackRange;
    }

    private boolean canRun(PathAwareEntity entity) {
        return this.getAttackTarget(entity) != null && entity.getMainHandStack().contains(DataComponentTypes.KINETIC_WEAPON);
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, PathAwareEntity arg2) {
        return this.canRun(arg2) && !arg2.isUsingItem();
    }

    @Override
    protected void run(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.setAttacking(true);
        arg2.getBrain().remember(MemoryModuleType.SPEAR_STATUS, SpearChargeTask.AdvanceState.APPROACH);
        super.run(arg, arg2, l);
    }

    private @Nullable LivingEntity getAttackTarget(PathAwareEntity entity) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        return this.canRun(arg2) && this.isTargetWithinRange(arg2);
    }

    private boolean isTargetWithinRange(PathAwareEntity entity) {
        LivingEntity lv = this.getAttackTarget(entity);
        double d = entity.squaredDistanceTo(lv.getX(), lv.getY(), lv.getZ());
        return d > (double)this.squaredAttackRange;
    }

    @Override
    protected void keepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        LivingEntity lv = this.getAttackTarget(arg2);
        Entity lv2 = arg2.getRootVehicle();
        float f = 1.0f;
        if (lv2 instanceof MobEntity) {
            MobEntity lv3 = (MobEntity)lv2;
            f = lv3.getRiderChargingSpeedMultiplier();
        }
        arg2.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(lv, true));
        arg2.getNavigation().startMovingTo(lv, (double)f * this.speed);
    }

    @Override
    protected void finishRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.getNavigation().stop();
        arg2.getBrain().remember(MemoryModuleType.SPEAR_STATUS, SpearChargeTask.AdvanceState.CHARGING);
    }

    @Override
    protected boolean isTimeLimitExceeded(long time) {
        return false;
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

