/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;point(Ljava/lang/Object;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/Brain;doExclusively(Lnet/minecraft/entity/ai/brain/Activity;)V
 */
package net.minecraft.entity.ai.brain.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.village.raid.Raid;

public class StartRaidTask {
    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(context -> context.point((world, entity, time) -> {
            if (world.random.nextInt(20) != 0) {
                return false;
            }
            Brain<?> lv = entity.getBrain();
            Raid lv2 = world.getRaidAt(entity.getBlockPos());
            if (lv2 != null) {
                if (!lv2.hasSpawned() || lv2.isPreRaid()) {
                    lv.setDefaultActivity(Activity.PRE_RAID);
                    lv.doExclusively(Activity.PRE_RAID);
                } else {
                    lv.setDefaultActivity(Activity.RAID);
                    lv.doExclusively(Activity.RAID);
                }
            }
            return true;
        }));
    }
}

