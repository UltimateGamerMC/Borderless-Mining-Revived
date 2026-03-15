/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;point(Ljava/lang/Object;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/util/collection/WeightedList;shuffle()Lnet/minecraft/util/collection/WeightedList;
 *   Lnet/minecraft/entity/ai/brain/task/TaskRunnable;trigger(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/Tasks;weighted(Ljava/util/List;Lnet/minecraft/entity/ai/brain/task/CompositeTask$Order;Lnet/minecraft/entity/ai/brain/task/CompositeTask$RunMode;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 */
package net.minecraft.entity.ai.brain.task;

import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CompositeTask;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskRunnable;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.util.collection.WeightedList;

public class Tasks {
    public static <E extends LivingEntity> SingleTickTask<E> pickRandomly(List<Pair<? extends TaskRunnable<? super E>, Integer>> weightedTasks) {
        return Tasks.weighted(weightedTasks, CompositeTask.Order.SHUFFLED, CompositeTask.RunMode.RUN_ONE);
    }

    public static <E extends LivingEntity> SingleTickTask<E> weighted(List<Pair<? extends TaskRunnable<? super E>, Integer>> weightedTasks, CompositeTask.Order order, CompositeTask.RunMode runMode) {
        WeightedList lv = new WeightedList();
        weightedTasks.forEach(task -> lv.add((TaskRunnable)task.getFirst(), (Integer)task.getSecond()));
        return TaskTriggerer.task(context -> context.point((world, entity, time) -> {
            TaskRunnable lv;
            if (order == CompositeTask.Order.SHUFFLED) {
                lv.shuffle();
            }
            Iterator iterator = lv.iterator();
            while (iterator.hasNext() && (!(lv = (TaskRunnable)iterator.next()).trigger(world, entity, time) || runMode != CompositeTask.RunMode.RUN_ONE)) {
            }
            return true;
        }));
    }
}

