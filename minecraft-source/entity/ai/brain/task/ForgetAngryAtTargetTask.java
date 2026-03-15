/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.world.rule.GameRules;

public class ForgetAngryAtTargetTask {
    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.ANGRY_AT)).apply(context, angryAt -> (world, entity, time) -> {
            Optional.ofNullable(world.getEntity((UUID)context.getValue(angryAt))).map(target -> {
                LivingEntity lv;
                return target instanceof LivingEntity ? (lv = (LivingEntity)target) : null;
            }).filter(LivingEntity::isDead).filter(target -> target.getType() != EntityType.PLAYER || world.getGameRules().getValue(GameRules.FORGIVE_DEAD_PLAYERS) != false).ifPresent(target -> angryAt.forget());
            return true;
        }));
    }
}

