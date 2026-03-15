/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/VillagerWorkTask;performAdditionalWork(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;)V
 *   Lnet/minecraft/entity/ai/brain/task/VillagerWorkTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;

public class VillagerWorkTask
extends MultiTickTask<VillagerEntity> {
    private static final int RUN_TIME = 300;
    private static final double MAX_DISTANCE = 1.73;
    private long lastCheckedTime;

    public VillagerWorkTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED));
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, VillagerEntity arg2) {
        if (arg.getTime() - this.lastCheckedTime < 300L) {
            return false;
        }
        if (arg.random.nextInt(2) != 0) {
            return false;
        }
        this.lastCheckedTime = arg.getTime();
        GlobalPos lv = arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get();
        return lv.dimension() == arg.getRegistryKey() && lv.pos().isWithinDistance(arg2.getEntityPos(), 1.73);
    }

    @Override
    protected void run(ServerWorld arg, VillagerEntity arg2, long l) {
        Brain<VillagerEntity> lv = arg2.getBrain();
        lv.remember(MemoryModuleType.LAST_WORKED_AT_POI, l);
        lv.getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).ifPresent(pos -> lv.remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(pos.pos())));
        arg2.playWorkSound();
        this.performAdditionalWork(arg, arg2);
        if (arg2.shouldRestock(arg)) {
            arg2.restock();
        }
    }

    protected void performAdditionalWork(ServerWorld world, VillagerEntity entity) {
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        Optional<GlobalPos> optional = arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
        if (optional.isEmpty()) {
            return false;
        }
        GlobalPos lv = optional.get();
        return lv.dimension() == arg.getRegistryKey() && lv.pos().isWithinDistance(arg2.getEntityPos(), 1.73);
    }

    @Override
    protected /* synthetic */ boolean shouldRun(ServerWorld world, LivingEntity entity) {
        return this.shouldRun(world, (VillagerEntity)entity);
    }

    @Override
    protected /* synthetic */ boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return this.shouldKeepRunning(world, (VillagerEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void run(ServerWorld world, LivingEntity entity, long time) {
        this.run(world, (VillagerEntity)entity, time);
    }
}

