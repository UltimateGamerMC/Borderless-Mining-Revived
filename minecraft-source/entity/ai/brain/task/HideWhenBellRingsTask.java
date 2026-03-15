/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
 *   Lnet/minecraft/entity/ai/brain/Brain;doExclusively(Lnet/minecraft/entity/ai/brain/Activity;)V
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.village.raid.Raid;

public class HideWhenBellRingsTask {
    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.HEARD_BELL_TIME)).apply(context, heardBellTime -> (world, entity, time) -> {
            Raid lv = world.getRaidAt(entity.getBlockPos());
            if (lv == null) {
                entity.getBrain().doExclusively(Activity.HIDE);
            }
            return true;
        }));
    }
}

