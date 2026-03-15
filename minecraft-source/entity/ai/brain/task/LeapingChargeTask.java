/*
 * External method calls:
 *   Lnet/minecraft/server/world/ServerWorld;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/LeapingChargeTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/LeapingChargeTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class LeapingChargeTask
extends MultiTickTask<MobEntity> {
    public static final int RUN_TIME = 100;
    private final UniformIntProvider cooldownRange;
    private final SoundEvent sound;

    public LeapingChargeTask(UniformIntProvider cooldownRange, SoundEvent sound) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleState.VALUE_PRESENT), 100);
        this.cooldownRange = cooldownRange;
        this.sound = sound;
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, MobEntity arg2, long l) {
        return !arg2.isOnGround();
    }

    @Override
    protected void run(ServerWorld arg, MobEntity arg2, long l) {
        arg2.setNoDrag(true);
        arg2.setPose(EntityPose.LONG_JUMPING);
    }

    @Override
    protected void finishRunning(ServerWorld arg, MobEntity arg2, long l) {
        if (arg2.isOnGround()) {
            arg2.setVelocity(arg2.getVelocity().multiply(0.1f, 1.0, 0.1f));
            arg.playSoundFromEntity(null, arg2, this.sound, SoundCategory.NEUTRAL, 2.0f, 1.0f);
        }
        arg2.setNoDrag(false);
        arg2.setPose(EntityPose.STANDING);
        arg2.getBrain().forget(MemoryModuleType.LONG_JUMP_MID_JUMP);
        arg2.getBrain().remember(MemoryModuleType.LONG_JUMP_COOLING_DOWN, this.cooldownRange.get(arg.random));
    }

    @Override
    protected /* synthetic */ void finishRunning(ServerWorld world, LivingEntity entity, long time) {
        this.finishRunning(world, (MobEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void run(ServerWorld world, LivingEntity entity, long time) {
        this.run(world, (MobEntity)entity, time);
    }
}

