/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;createProfile(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/ai/brain/task/UpdateAttackTargetTask;create(Lnet/minecraft/entity/ai/brain/task/UpdateAttackTargetTask$TargetGetter;)Lnet/minecraft/entity/ai/brain/task/Task;
 *   Lnet/minecraft/entity/ai/brain/task/StrollTask;createDynamicRadius(F)Lnet/minecraft/entity/ai/brain/task/Task;
 *   Lnet/minecraft/entity/ai/brain/task/GoToLookTargetTask;create(FI)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/Brain;resetPossibleActivities(Ljava/util/List;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;addCoreActivities(Lnet/minecraft/entity/ai/brain/Brain;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;addIdleActivities(Lnet/minecraft/entity/ai/brain/Brain;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;addFightActivities(Lnet/minecraft/entity/ai/brain/Brain;)V
 */
package net.minecraft.entity.mob;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.CompositeTask;
import net.minecraft.entity.ai.brain.task.DashAttackTask;
import net.minecraft.entity.ai.brain.task.GoToLookTargetTask;
import net.minecraft.entity.ai.brain.task.MoveToTargetTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.TemptTask;
import net.minecraft.entity.ai.brain.task.TickCooldownTask;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.UpdateLookControlTask;
import net.minecraft.entity.mob.ZombieNautilusEntity;
import net.minecraft.entity.passive.NautilusBrain;
import net.minecraft.sound.SoundEvents;

public class ZombieNautilusBrain {
    private static final float field_63366 = 1.0f;
    private static final float field_63367 = 0.9f;
    private static final float field_63368 = 0.5f;
    private static final float field_63369 = 2.0f;
    private static final int field_63370 = 80;
    private static final double field_63371 = 12.0;
    private static final double field_63372 = 11.0;
    protected static final ImmutableList<SensorType<? extends Sensor<? super ZombieNautilusEntity>>> SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.ATTACK_TARGET, new MemoryModuleType[]{MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN});

    protected static Brain.Profile<ZombieNautilusEntity> createProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    protected static Brain<?> create(Brain<ZombieNautilusEntity> brain) {
        ZombieNautilusBrain.addCoreActivities(brain);
        ZombieNautilusBrain.addIdleActivities(brain);
        ZombieNautilusBrain.addFightActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<ZombieNautilusEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new UpdateLookControlTask(45, 90), new MoveToTargetTask(), new TickCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new TickCooldownTask(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new TickCooldownTask(MemoryModuleType.ATTACK_TARGET_COOLDOWN)));
    }

    private static void addIdleActivities(Brain<ZombieNautilusEntity> brain) {
        brain.setTaskList(Activity.IDLE, ImmutableList.of(Pair.of(1, new TemptTask(entity -> Float.valueOf(0.9f), entity -> entity.isBaby() ? 2.5 : 3.5)), Pair.of(2, UpdateAttackTargetTask.create(NautilusBrain::findAttackTarget)), Pair.of(3, new CompositeTask(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT), ImmutableSet.of(), CompositeTask.Order.ORDERED, CompositeTask.RunMode.TRY_ALL, ImmutableList.of(Pair.of(StrollTask.createDynamicRadius(1.0f), 2), Pair.of(GoToLookTargetTask.create(1.0f, 3), 3))))));
    }

    private static void addFightActivities(Brain<ZombieNautilusEntity> brain) {
        brain.setTaskList(Activity.FIGHT, ImmutableList.of(Pair.of(0, new DashAttackTask(80, NautilusBrain.FIGHT_TARGET_PREDICATE, 0.5f, 2.0f, 12.0, 11.0, SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DASH))), ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT), Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT)));
    }

    public static void updateActivities(ZombieNautilusEntity nautilus) {
        nautilus.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }
}

