/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 *   Lnet/minecraft/entity/ai/brain/task/FindPointOfInterestTask;findPathToPoi(Lnet/minecraft/entity/mob/MobEntity;Ljava/util/Set;)Lnet/minecraft/entity/ai/pathing/Path;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *   Lnet/minecraft/server/debug/SubscriptionTracker;onPoiUpdated(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z
 */
package net.minecraft.entity.ai.brain.task;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.FindPointOfInterestTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

public class GoToHomeTask {
    private static final int POI_EXPIRY = 40;
    private static final int MAX_TRIES = 5;
    private static final int RUN_TIME = 20;
    private static final int MAX_DISTANCE = 4;

    public static Task<PathAwareEntity> create(float speed) {
        Long2LongOpenHashMap long2LongMap = new Long2LongOpenHashMap();
        MutableLong mutableLong = new MutableLong(0L);
        return TaskTriggerer.task(arg -> arg.group(arg.queryMemoryAbsent(MemoryModuleType.WALK_TARGET), arg.queryMemoryAbsent(MemoryModuleType.HOME)).apply(arg, (walkTarget, home) -> (world, entity, time) -> {
            if (world.getTime() - mutableLong.longValue() < 20L) {
                return false;
            }
            PointOfInterestStorage lv = world.getPointOfInterestStorage();
            Optional<BlockPos> optional = lv.getNearestPosition(poiType -> poiType.matchesKey(PointOfInterestTypes.HOME), entity.getBlockPos(), 48, PointOfInterestStorage.OccupationStatus.ANY);
            if (optional.isEmpty() || optional.get().getSquaredDistance(entity.getBlockPos()) <= 4.0) {
                return false;
            }
            MutableInt mutableInt = new MutableInt(0);
            mutableLong.setValue(world.getTime() + (long)world.getRandom().nextInt(20));
            Predicate<BlockPos> predicate = pos -> {
                long l = pos.asLong();
                if (long2LongMap.containsKey(l)) {
                    return false;
                }
                if (mutableInt.incrementAndGet() >= 5) {
                    return false;
                }
                long2LongMap.put(l, mutableLong.longValue() + 40L);
                return true;
            };
            Set<Pair<RegistryEntry<PointOfInterestType>, BlockPos>> set = lv.getTypesAndPositions(poiType -> poiType.matchesKey(PointOfInterestTypes.HOME), predicate, entity.getBlockPos(), 48, PointOfInterestStorage.OccupationStatus.ANY).collect(Collectors.toSet());
            Path lv2 = FindPointOfInterestTask.findPathToPoi(entity, set);
            if (lv2 != null && lv2.reachesTarget()) {
                BlockPos lv3 = lv2.getTarget();
                Optional<RegistryEntry<PointOfInterestType>> optional2 = lv.getType(lv3);
                if (optional2.isPresent()) {
                    walkTarget.remember(new WalkTarget(lv3, speed, 1));
                    world.getSubscriptionTracker().onPoiUpdated(lv3);
                }
            } else if (mutableInt.intValue() < 5) {
                long2LongMap.long2LongEntrySet().removeIf(entry -> entry.getLongValue() < mutableLong.longValue());
            }
            return true;
        }));
    }
}

