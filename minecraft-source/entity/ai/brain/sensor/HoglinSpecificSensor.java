/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/util/Optional;)V
 *   Lnet/minecraft/entity/ai/brain/LivingTargetCache;empty()Lnet/minecraft/entity/ai/brain/LivingTargetCache;
 *   Lnet/minecraft/entity/ai/brain/LivingTargetCache;iterate(Ljava/util/function/Predicate;)Ljava/lang/Iterable;
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/sensor/HoglinSpecificSensor;findNearestWarpedFungus(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/HoglinEntity;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/ai/brain/sensor/HoglinSpecificSensor;sense(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/HoglinEntity;)V
 */
package net.minecraft.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HoglinSpecificSensor
extends Sensor<HoglinEntity> {
    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, new MemoryModuleType[0]);
    }

    @Override
    protected void sense(ServerWorld arg2, HoglinEntity arg22) {
        Brain<HoglinEntity> lv = arg22.getBrain();
        lv.remember(MemoryModuleType.NEAREST_REPELLENT, this.findNearestWarpedFungus(arg2, arg22));
        Optional<Object> optional = Optional.empty();
        int i = 0;
        ArrayList<HoglinEntity> list = Lists.newArrayList();
        LivingTargetCache lv2 = lv.getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        for (LivingEntity lv3 : lv2.iterate(arg -> !arg.isBaby() && (arg instanceof PiglinEntity || arg instanceof HoglinEntity))) {
            if (lv3 instanceof PiglinEntity) {
                PiglinEntity lv4 = (PiglinEntity)lv3;
                ++i;
                if (optional.isEmpty()) {
                    optional = Optional.of(lv4);
                }
            }
            if (!(lv3 instanceof HoglinEntity)) continue;
            HoglinEntity lv5 = (HoglinEntity)lv3;
            list.add(lv5);
        }
        lv.remember(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, optional);
        lv.remember(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, list);
        lv.remember(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, i);
        lv.remember(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, list.size());
    }

    private Optional<BlockPos> findNearestWarpedFungus(ServerWorld world, HoglinEntity hoglin) {
        return BlockPos.findClosest(hoglin.getBlockPos(), 8, 4, pos -> world.getBlockState((BlockPos)pos).isIn(BlockTags.HOGLIN_REPELLENTS));
    }
}

