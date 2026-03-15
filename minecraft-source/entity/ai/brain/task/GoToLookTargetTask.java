/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/GoToLookTargetTask;create(Ljava/util/function/Predicate;Ljava/util/function/Function;I)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.LookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;

public class GoToLookTargetTask {
    public static SingleTickTask<LivingEntity> create(float speed, int completionRange) {
        return GoToLookTargetTask.create(entity -> true, entity -> Float.valueOf(speed), completionRange);
    }

    public static SingleTickTask<LivingEntity> create(Predicate<LivingEntity> predicate, Function<LivingEntity, Float> speed, int completionRange) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.WALK_TARGET), context.queryMemoryValue(MemoryModuleType.LOOK_TARGET)).apply(context, (walkTarget, lookTarget) -> (world, entity, time) -> {
            if (!predicate.test(entity)) {
                return false;
            }
            walkTarget.remember(new WalkTarget((LookTarget)context.getValue(lookTarget), ((Float)speed.apply(entity)).floatValue(), completionRange));
            return true;
        }));
    }
}

