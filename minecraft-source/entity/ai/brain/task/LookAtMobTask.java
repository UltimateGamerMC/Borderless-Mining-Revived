/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 *   Lnet/minecraft/entity/ai/brain/LivingTargetCache;findFirst(Ljava/util/function/Predicate;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/LivingEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/LookAtMobTask;create(Ljava/util/function/Predicate;F)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;

public class LookAtMobTask {
    public static Task<LivingEntity> create(SpawnGroup spawnGroup, float maxDistance) {
        return LookAtMobTask.create((LivingEntity entity) -> spawnGroup.equals(entity.getType().getSpawnGroup()), maxDistance);
    }

    public static SingleTickTask<LivingEntity> create(EntityType<?> type, float maxDistance) {
        return LookAtMobTask.create((LivingEntity entity) -> type.equals(entity.getType()), maxDistance);
    }

    public static SingleTickTask<LivingEntity> create(float maxDistance) {
        return LookAtMobTask.create((LivingEntity entity) -> true, maxDistance);
    }

    public static SingleTickTask<LivingEntity> create(Predicate<LivingEntity> predicate, float maxDistance) {
        float g = maxDistance * maxDistance;
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.LOOK_TARGET), context.queryMemoryValue(MemoryModuleType.VISIBLE_MOBS)).apply(context, (lookTarget, visibleMobs) -> (world, entity, time) -> {
            Optional<LivingEntity> optional = ((LivingTargetCache)context.getValue(visibleMobs)).findFirst(predicate.and(target -> target.squaredDistanceTo(entity) <= (double)g && !entity.hasPassenger((Entity)target)));
            if (optional.isEmpty()) {
                return false;
            }
            lookTarget.remember(new EntityLookTarget(optional.get(), true));
            return true;
        }));
    }
}

