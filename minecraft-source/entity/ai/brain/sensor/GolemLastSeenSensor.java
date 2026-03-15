/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;J)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/sensor/GolemLastSeenSensor;senseIronGolem(Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/ai/brain/sensor/GolemLastSeenSensor;rememberIronGolem(Lnet/minecraft/entity/LivingEntity;)V
 */
package net.minecraft.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

public class GolemLastSeenSensor
extends Sensor<LivingEntity> {
    private static final int RUN_TIME = 200;
    private static final int GOLEM_DETECTED_WARMUP = 599;

    public GolemLastSeenSensor() {
        this(200);
    }

    public GolemLastSeenSensor(int i) {
        super(i);
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        GolemLastSeenSensor.senseIronGolem(entity);
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.MOBS);
    }

    public static void senseIronGolem(LivingEntity entity) {
        Optional<List<LivingEntity>> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.MOBS);
        if (optional.isEmpty()) {
            return;
        }
        boolean bl = optional.get().stream().anyMatch(seenEntity -> seenEntity.getType().equals(EntityType.IRON_GOLEM));
        if (bl) {
            GolemLastSeenSensor.rememberIronGolem(entity);
        }
    }

    public static void rememberIronGolem(LivingEntity entity) {
        entity.getBrain().remember(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 599L);
    }
}

