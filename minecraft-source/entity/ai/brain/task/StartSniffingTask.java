/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P5;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;J)V
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.util.Unit;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class StartSniffingTask {
    private static final IntProvider COOLDOWN = UniformIntProvider.create(100, 200);

    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryOptional(MemoryModuleType.IS_SNIFFING), context.queryMemoryOptional(MemoryModuleType.WALK_TARGET), context.queryMemoryAbsent(MemoryModuleType.SNIFF_COOLDOWN), context.queryMemoryValue(MemoryModuleType.NEAREST_ATTACKABLE), context.queryMemoryAbsent(MemoryModuleType.DISTURBANCE_LOCATION)).apply(context, (isSniffing, walkTarget, sniffCooldown, nearestAttackable, disturbanceLocation) -> (world, entity, time) -> {
            isSniffing.remember(Unit.INSTANCE);
            sniffCooldown.remember(Unit.INSTANCE, COOLDOWN.get(world.getRandom()));
            walkTarget.forget();
            entity.setPose(EntityPose.SNIFFING);
            return true;
        }));
    }
}

