/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.PiglinEntity;

public class WantNewItemTask<E extends PiglinEntity> {
    public static Task<LivingEntity> create(int range) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.ADMIRING_ITEM), context.queryMemoryOptional(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)).apply(context, (admiringItem, nearestVisibleWantedItem) -> (world, entity, time) -> {
            if (!entity.getOffHandStack().isEmpty()) {
                return false;
            }
            Optional optional = context.getOptionalValue(nearestVisibleWantedItem);
            if (optional.isPresent() && ((ItemEntity)optional.get()).isInRange(entity, range)) {
                return false;
            }
            admiringItem.forget();
            return true;
        }));
    }
}

