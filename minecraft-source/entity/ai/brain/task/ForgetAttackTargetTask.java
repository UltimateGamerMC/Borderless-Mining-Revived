/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 *   Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask$AlternativeCondition;test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z
 *   Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask$ForgetCallback;accept(Lnet/minecraft/server/world/ServerWorld;Ljava/lang/Object;Lnet/minecraft/entity/LivingEntity;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask;create(Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask$AlternativeCondition;Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask$ForgetCallback;Z)Lnet/minecraft/entity/ai/brain/task/Task;
 *   Lnet/minecraft/entity/ai/brain/task/ForgetAttackTargetTask;cannotReachTarget(Lnet/minecraft/entity/LivingEntity;Ljava/util/Optional;)Z
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

public class ForgetAttackTargetTask {
    private static final int REMEMBER_TIME = 200;

    public static <E extends MobEntity> Task<E> create(ForgetCallback<E> callback) {
        return ForgetAttackTargetTask.create((world, target) -> false, callback, true);
    }

    public static <E extends MobEntity> Task<E> create(AlternativeCondition condition) {
        return ForgetAttackTargetTask.create(condition, (world, entity, target) -> {}, true);
    }

    public static <E extends MobEntity> Task<E> create() {
        return ForgetAttackTargetTask.create((world, target) -> false, (world, entity, target) -> {}, true);
    }

    public static <E extends MobEntity> Task<E> create(AlternativeCondition condition, ForgetCallback<E> callback, boolean shouldForgetIfTargetUnreachable) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.ATTACK_TARGET), context.queryMemoryOptional(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply(context, (attackTarget, cantReachWalkTargetSince) -> (world, entity, time) -> {
            LivingEntity lv = (LivingEntity)context.getValue(attackTarget);
            if (!entity.canTarget(lv) || shouldForgetIfTargetUnreachable && ForgetAttackTargetTask.cannotReachTarget(entity, context.getOptionalValue(cantReachWalkTargetSince)) || !lv.isAlive() || lv.getEntityWorld() != entity.getEntityWorld() || condition.test(world, lv)) {
                callback.accept(world, entity, lv);
                attackTarget.forget();
                return true;
            }
            return true;
        }));
    }

    private static boolean cannotReachTarget(LivingEntity target, Optional<Long> lastReachTime) {
        return lastReachTime.isPresent() && target.getEntityWorld().getTime() - lastReachTime.get() > 200L;
    }

    @FunctionalInterface
    public static interface AlternativeCondition {
        public boolean test(ServerWorld var1, LivingEntity var2);
    }

    @FunctionalInterface
    public static interface ForgetCallback<E> {
        public void accept(ServerWorld var1, E var2, LivingEntity var3);
    }
}

