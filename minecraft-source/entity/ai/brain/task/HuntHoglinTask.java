/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;
 *   Lnet/minecraft/entity/mob/PiglinBrain;becomeAngryWith(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/AbstractPiglinEntity;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/mob/PiglinBrain;rememberHunting(Lnet/minecraft/entity/mob/AbstractPiglinEntity;)V
 *   Lnet/minecraft/entity/mob/PiglinBrain;angerAtCloserTargets(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/AbstractPiglinEntity;Lnet/minecraft/entity/LivingEntity;)V
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;

public class HuntHoglinTask {
    public static SingleTickTask<PiglinEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN), context.queryMemoryAbsent(MemoryModuleType.ANGRY_AT), context.queryMemoryAbsent(MemoryModuleType.HUNTED_RECENTLY), context.queryMemoryOptional(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)).apply(context, (nearestVisibleHuntableHoglin, angryAt, huntedRecently, nearestVisibleAdultPiglins) -> (world, entity, time) -> {
            if (entity.isBaby() || context.getOptionalValue(nearestVisibleAdultPiglins).map(piglin -> piglin.stream().anyMatch(HuntHoglinTask::hasHuntedRecently)).isPresent()) {
                return false;
            }
            HoglinEntity lv = (HoglinEntity)context.getValue(nearestVisibleHuntableHoglin);
            PiglinBrain.becomeAngryWith(world, entity, lv);
            PiglinBrain.rememberHunting(entity);
            PiglinBrain.angerAtCloserTargets(world, entity, lv);
            context.getOptionalValue(nearestVisibleAdultPiglins).ifPresent(piglin -> piglin.forEach(PiglinBrain::rememberHunting));
            return true;
        }));
    }

    private static boolean hasHuntedRecently(AbstractPiglinEntity piglin) {
        return piglin.getBrain().hasMemoryModule(MemoryModuleType.HUNTED_RECENTLY);
    }
}

