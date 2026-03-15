/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.function.BiPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;

public class RidingTask {
    public static <E extends LivingEntity> Task<E> create(int range, BiPredicate<E, Entity> alternativeRideCondition) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryOptional(MemoryModuleType.RIDE_TARGET)).apply(context, rideTarget -> (world, entity, time) -> {
            Entity lv3;
            Entity lv = entity.getVehicle();
            Entity lv2 = context.getOptionalValue(rideTarget).orElse(null);
            if (lv == null && lv2 == null) {
                return false;
            }
            Entity entity2 = lv3 = lv == null ? lv2 : lv;
            if (!RidingTask.canRideTarget(entity, lv3, range) || alternativeRideCondition.test(entity, lv3)) {
                entity.stopRiding();
                rideTarget.forget();
                return true;
            }
            return false;
        }));
    }

    private static boolean canRideTarget(LivingEntity entity, Entity vehicle, int range) {
        return vehicle.isAlive() && vehicle.isInRange(entity, range) && vehicle.getEntityWorld() == entity.getEntityWorld();
    }
}

