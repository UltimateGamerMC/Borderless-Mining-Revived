/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;
 *   Lnet/minecraft/entity/ai/brain/task/TargetUtil;walkTowards(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/math/BlockPos;FI)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/WalkTowardsFuzzyPosTask;fuzz(Lnet/minecraft/util/math/random/Random;)I
 *   Lnet/minecraft/entity/ai/brain/task/WalkTowardsFuzzyPosTask;fuzz(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class WalkTowardsFuzzyPosTask {
    private static BlockPos fuzz(MobEntity mob, BlockPos pos) {
        Random lv = mob.getEntityWorld().random;
        return pos.add(WalkTowardsFuzzyPosTask.fuzz(lv), 0, WalkTowardsFuzzyPosTask.fuzz(lv));
    }

    private static int fuzz(Random random) {
        return random.nextInt(3) - 1;
    }

    public static <E extends MobEntity> SingleTickTask<E> create(MemoryModuleType<BlockPos> posModule, int completionRange, float speed) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(posModule), context.queryMemoryAbsent(MemoryModuleType.ATTACK_TARGET), context.queryMemoryAbsent(MemoryModuleType.WALK_TARGET), context.queryMemoryOptional(MemoryModuleType.LOOK_TARGET)).apply(context, (pos, attackTarget, walkTarget, lookTarget) -> (world, entity, time) -> {
            BlockPos lv = (BlockPos)context.getValue(pos);
            boolean bl = lv.isWithinDistance(entity.getBlockPos(), (double)completionRange);
            if (!bl) {
                TargetUtil.walkTowards(entity, WalkTowardsFuzzyPosTask.fuzz(entity, lv), speed, completionRange);
            }
            return true;
        }));
    }
}

