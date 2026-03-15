/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
 *   Lnet/minecraft/entity/mob/PiglinBrain;consumeOffHandItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PiglinEntity;Z)V
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;

public class RemoveOffHandItemTask {
    public static Task<PiglinEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.ADMIRING_ITEM)).apply(context, admiringItem -> (world, entity, time) -> {
            if (entity.getOffHandStack().isEmpty() || entity.getOffHandStack().contains(DataComponentTypes.BLOCKS_ATTACKS)) {
                return false;
            }
            PiglinBrain.consumeOffHandItem(world, entity, true);
            return true;
        }));
    }
}

