/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P3;
 *   Lnet/minecraft/entity/ai/brain/Brain;refreshActivities(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;JLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/LivingEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;

public class StopPanickingTask {
    private static final int MAX_DISTANCE = 36;

    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryOptional(MemoryModuleType.HURT_BY), context.queryMemoryOptional(MemoryModuleType.HURT_BY_ENTITY), context.queryMemoryOptional(MemoryModuleType.NEAREST_HOSTILE)).apply(context, (hurtBy, hurtByEntity, nearestHostile) -> (world, entity, time) -> {
            boolean bl;
            boolean bl2 = bl = context.getOptionalValue(hurtBy).isPresent() || context.getOptionalValue(nearestHostile).isPresent() || context.getOptionalValue(hurtByEntity).filter(hurtByx -> hurtByx.squaredDistanceTo(entity) <= 36.0).isPresent();
            if (!bl) {
                hurtBy.forget();
                hurtByEntity.forget();
                entity.getBrain().refreshActivities(world.getEnvironmentAttributes(), world.getTime(), entity.getEntityPos());
            }
            return true;
        }));
    }
}

