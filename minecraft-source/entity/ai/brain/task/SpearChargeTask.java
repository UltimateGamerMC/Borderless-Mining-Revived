/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/brain/task/MultiTickTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V
 *   Lnet/minecraft/entity/mob/PathAwareEntity;squaredDistanceTo(DDD)D
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(DDDD)Z
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(Lnet/minecraft/entity/Entity;D)Z
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;findFrom(Lnet/minecraft/entity/mob/PathAwareEntity;DDILnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/SpearChargeTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearChargeTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/SpearChargeTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Map;
import java.util.Optional;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class SpearChargeTask
extends MultiTickTask<PathAwareEntity> {
    public static final int field_64623 = 6;
    public static final int field_64624 = 7;
    double chargeStartSpeed;
    double chargeSpeed;
    float field_64627;
    float squaredChargeRange;

    public SpearChargeTask(double chargeStartSpeed, double chargeSpeed, float f, float chargeRange) {
        super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryModuleState.VALUE_PRESENT));
        this.chargeStartSpeed = chargeStartSpeed;
        this.chargeSpeed = chargeSpeed;
        this.field_64627 = f * f;
        this.squaredChargeRange = chargeRange * chargeRange;
    }

    private @Nullable LivingEntity getTarget(PathAwareEntity entity) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private boolean shouldAttack(PathAwareEntity entity) {
        return this.getTarget(entity) != null && entity.getMainHandStack().contains(DataComponentTypes.KINETIC_WEAPON);
    }

    private int getSpearUseTicks(PathAwareEntity entity) {
        return Optional.ofNullable(entity.getMainHandStack().get(DataComponentTypes.KINETIC_WEAPON)).map(KineticWeaponComponent::getUseTicks).orElse(0);
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, PathAwareEntity arg2) {
        return arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_STATUS).orElse(AdvanceState.APPROACH) == AdvanceState.CHARGING && this.shouldAttack(arg2) && !arg2.isUsingItem();
    }

    @Override
    protected void run(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.setAttacking(true);
        arg2.getBrain().remember(MemoryModuleType.SPEAR_ENGAGE_TIME, this.getSpearUseTicks(arg2));
        arg2.getBrain().forget(MemoryModuleType.SPEAR_CHARGE_POSITION);
        arg2.setCurrentHand(Hand.MAIN_HAND);
        super.run(arg, arg2, l);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        return arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(0) > 0 && this.shouldAttack(arg2);
    }

    @Override
    protected void keepRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        LivingEntity lv = this.getTarget(arg2);
        double d = arg2.squaredDistanceTo(lv.getX(), lv.getY(), lv.getZ());
        Entity lv2 = arg2.getRootVehicle();
        float f = 1.0f;
        if (lv2 instanceof MobEntity) {
            MobEntity lv3 = (MobEntity)lv2;
            f = lv3.getRiderChargingSpeedMultiplier();
        }
        int i = arg2.hasVehicle() ? 2 : 0;
        arg2.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(lv, true));
        arg2.getBrain().remember(MemoryModuleType.SPEAR_ENGAGE_TIME, arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(0) - 1);
        Vec3d lv4 = arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SPEAR_CHARGE_POSITION).orElse(null);
        if (lv4 != null) {
            arg2.getNavigation().startMovingTo(lv4.x, lv4.y, lv4.z, (double)f * this.chargeSpeed);
            if (arg2.getNavigation().isIdle()) {
                arg2.getBrain().forget(MemoryModuleType.SPEAR_CHARGE_POSITION);
            }
        } else {
            arg2.getNavigation().startMovingTo(lv, (double)f * this.chargeStartSpeed);
            if (d < (double)this.squaredChargeRange || arg2.getNavigation().isIdle()) {
                double e = Math.sqrt(d);
                Vec3d lv5 = FuzzyTargeting.findFrom(arg2, (double)(6 + i) - e, (double)(7 + i) - e, 7, lv.getEntityPos());
                arg2.getBrain().remember(MemoryModuleType.SPEAR_CHARGE_POSITION, lv5);
            }
        }
    }

    @Override
    protected void finishRunning(ServerWorld arg, PathAwareEntity arg2, long l) {
        arg2.getNavigation().stop();
        arg2.clearActiveItem();
        arg2.getBrain().forget(MemoryModuleType.SPEAR_CHARGE_POSITION);
        arg2.getBrain().forget(MemoryModuleType.SPEAR_ENGAGE_TIME);
        arg2.getBrain().remember(MemoryModuleType.SPEAR_STATUS, AdvanceState.RETREAT);
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

    public static enum AdvanceState {
        APPROACH,
        CHARGING,
        RETREAT;

    }
}

