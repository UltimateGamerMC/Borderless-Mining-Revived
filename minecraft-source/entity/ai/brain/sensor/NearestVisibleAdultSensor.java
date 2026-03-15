/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/LivingTargetCache;findFirst(Ljava/util/function/Predicate;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/util/Optional;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/sensor/NearestVisibleAdultSensor;find(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/brain/LivingTargetCache;)V
 */
package net.minecraft.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

public class NearestVisibleAdultSensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.VISIBLE_MOBS);
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).ifPresent(targetCache -> this.find(entity, (LivingTargetCache)targetCache));
    }

    protected void find(LivingEntity entity, LivingTargetCache targetCache) {
        Optional<LivingEntity> optional = targetCache.findFirst(target -> target.getType() == entity.getType() && !target.isBaby());
        entity.getBrain().remember(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
    }
}

