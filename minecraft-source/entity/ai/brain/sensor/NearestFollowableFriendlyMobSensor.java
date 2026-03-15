/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/LivingTargetCache;findFirst(Ljava/util/function/Predicate;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/util/Optional;)V
 */
package net.minecraft.entity.ai.brain.sensor;

import java.util.Optional;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleAdultSensor;
import net.minecraft.registry.tag.EntityTypeTags;

public class NearestFollowableFriendlyMobSensor
extends NearestVisibleAdultSensor {
    @Override
    protected void find(LivingEntity entity, LivingTargetCache targetCache) {
        Optional<LivingEntity> optional = targetCache.findFirst(potentialFriend -> potentialFriend.getType().isIn(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !potentialFriend.isBaby());
        entity.getBrain().remember(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
    }
}

