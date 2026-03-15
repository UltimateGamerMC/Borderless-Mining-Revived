/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/mob/MobEntity;swingHand(Lnet/minecraft/util/Hand;)V
 *   Lnet/minecraft/entity/mob/MobEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;J)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/MeleeAttackTask;create(Ljava/util/function/Predicate;I)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.function.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;

public class MeleeAttackTask {
    public static <T extends MobEntity> SingleTickTask<T> create(int cooldown) {
        return MeleeAttackTask.create(target -> true, cooldown);
    }

    public static <T extends MobEntity> SingleTickTask<T> create(Predicate<T> targetPredicate, int cooldown) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryOptional(MemoryModuleType.LOOK_TARGET), context.queryMemoryValue(MemoryModuleType.ATTACK_TARGET), context.queryMemoryAbsent(MemoryModuleType.ATTACK_COOLING_DOWN), context.queryMemoryValue(MemoryModuleType.VISIBLE_MOBS)).apply(context, (lookTarget, attackTarget, attackCoolingDown, visibleMobs) -> (world, entity, time) -> {
            LivingEntity lv = (LivingEntity)context.getValue(attackTarget);
            if (targetPredicate.test(entity) && !MeleeAttackTask.isHoldingUsableRangedWeapon(entity) && entity.isInAttackRange(lv) && ((LivingTargetCache)context.getValue(visibleMobs)).contains(lv)) {
                lookTarget.remember(new EntityLookTarget(lv, true));
                entity.swingHand(Hand.MAIN_HAND);
                entity.tryAttack(world, lv);
                attackCoolingDown.remember(true, cooldown);
                return true;
            }
            return false;
        }));
    }

    private static boolean isHoldingUsableRangedWeapon(MobEntity mob) {
        return mob.isHolding(mob::canUseRangedWeapon);
    }
}

