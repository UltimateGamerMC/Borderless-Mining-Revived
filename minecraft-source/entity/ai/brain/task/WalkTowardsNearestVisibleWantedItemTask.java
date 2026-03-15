/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/WalkTowardsNearestVisibleWantedItemTask;create(Ljava/util/function/Predicate;FZI)Lnet/minecraft/entity/ai/brain/task/Task;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.function.Predicate;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;

public class WalkTowardsNearestVisibleWantedItemTask {
    public static Task<LivingEntity> create(float speed, boolean requiresWalkTarget, int radius) {
        return WalkTowardsNearestVisibleWantedItemTask.create(entity -> true, speed, requiresWalkTarget, radius);
    }

    public static <E extends LivingEntity> Task<E> create(Predicate<E> startCondition, float speed, boolean requiresWalkTarget, int radius) {
        return TaskTriggerer.task(context -> {
            TaskTriggerer lv = requiresWalkTarget ? context.queryMemoryOptional(MemoryModuleType.WALK_TARGET) : context.queryMemoryAbsent(MemoryModuleType.WALK_TARGET);
            return context.group(context.queryMemoryOptional(MemoryModuleType.LOOK_TARGET), lv, context.queryMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), context.queryMemoryOptional(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)).apply(context, (lookTarget, walkTarget, nearestVisibleWantedItem, itemPickupCooldownTicks) -> (world, entity, time) -> {
                ItemEntity lv = (ItemEntity)context.getValue(nearestVisibleWantedItem);
                if (context.getOptionalValue(itemPickupCooldownTicks).isEmpty() && startCondition.test(entity) && lv.isInRange(entity, radius) && entity.getEntityWorld().getWorldBorder().contains(lv.getBlockPos()) && entity.canPickUpLoot()) {
                    WalkTarget lv2 = new WalkTarget(new EntityLookTarget(lv, false), speed, 0);
                    lookTarget.remember(new EntityLookTarget(lv, true));
                    walkTarget.remember(lv2);
                    return true;
                }
                return false;
            });
        });
    }
}

