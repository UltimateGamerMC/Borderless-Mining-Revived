/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/control/LookControl;lookAt(Lnet/minecraft/util/math/Vec3d;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/UpdateLookControlTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/UpdateLookControlTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

public class UpdateLookControlTask
extends MultiTickTask<MobEntity> {
    public UpdateLookControlTask(int minRunTime, int maxRunTime) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_PRESENT), minRunTime, maxRunTime);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, MobEntity arg2, long l) {
        return arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.LOOK_TARGET).filter(lookTarget -> lookTarget.isSeenBy(arg2)).isPresent();
    }

    @Override
    protected void finishRunning(ServerWorld arg, MobEntity arg2, long l) {
        arg2.getBrain().forget(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void keepRunning(ServerWorld arg, MobEntity arg2, long l) {
        arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.LOOK_TARGET).ifPresent(lookTarget -> arg2.getLookControl().lookAt(lookTarget.getPos()));
    }
}

