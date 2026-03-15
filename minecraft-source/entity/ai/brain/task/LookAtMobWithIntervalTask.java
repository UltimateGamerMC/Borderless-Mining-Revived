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
 *   Lnet/minecraft/entity/ai/brain/task/LookAtMobWithIntervalTask;follow(FLnet/minecraft/util/math/intprovider/UniformIntProvider;Ljava/util/function/Predicate;)Lnet/minecraft/entity/ai/brain/task/Task;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;

@Deprecated
public class LookAtMobWithIntervalTask {
    public static Task<LivingEntity> follow(float maxDistance, UniformIntProvider interval) {
        return LookAtMobWithIntervalTask.follow(maxDistance, interval, (LivingEntity entity) -> true);
    }

    public static Task<LivingEntity> follow(EntityType<?> type, float maxDistance, UniformIntProvider interval) {
        return LookAtMobWithIntervalTask.follow(maxDistance, interval, (LivingEntity entity) -> type.equals(entity.getType()));
    }

    private static Task<LivingEntity> follow(float maxDistance, UniformIntProvider interval, Predicate<LivingEntity> predicate) {
        float g = maxDistance * maxDistance;
        Interval lv = new Interval(interval);
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.LOOK_TARGET), context.queryMemoryValue(MemoryModuleType.VISIBLE_MOBS)).apply(context, (lookTarget, visibleMobs) -> (world, entity, time) -> {
            Optional<LivingEntity> optional = ((LivingTargetCache)context.getValue(visibleMobs)).findFirst(predicate.and(other -> other.squaredDistanceTo(entity) <= (double)g));
            if (optional.isEmpty()) {
                return false;
            }
            if (!lv.shouldRun(world.random)) {
                return false;
            }
            lookTarget.remember(new EntityLookTarget(optional.get(), true));
            return true;
        }));
    }

    public static final class Interval {
        private final UniformIntProvider interval;
        private int remainingTicks;

        public Interval(UniformIntProvider interval) {
            if (interval.getMin() <= 1) {
                throw new IllegalArgumentException();
            }
            this.interval = interval;
        }

        public boolean shouldRun(Random random) {
            if (this.remainingTicks == 0) {
                this.remainingTicks = this.interval.get(random) - 1;
                return false;
            }
            return --this.remainingTicks == 0;
        }
    }
}

