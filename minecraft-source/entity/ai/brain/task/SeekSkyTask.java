/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryAbsent(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/SeekSkyTask;findNearbySky(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import org.jspecify.annotations.Nullable;

public class SeekSkyTask {
    public static SingleTickTask<LivingEntity> create(float speed) {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.WALK_TARGET)).apply(context, walkTarget -> (world, entity, time) -> {
            if (world.isSkyVisible(entity.getBlockPos())) {
                return false;
            }
            Optional<Vec3d> optional = Optional.ofNullable(SeekSkyTask.findNearbySky(world, entity));
            optional.ifPresent(pos -> walkTarget.remember(new WalkTarget((Vec3d)pos, speed, 0)));
            return true;
        }));
    }

    private static @Nullable Vec3d findNearbySky(ServerWorld world, LivingEntity entity) {
        Random lv = entity.getRandom();
        BlockPos lv2 = entity.getBlockPos();
        for (int i = 0; i < 10; ++i) {
            BlockPos lv3 = lv2.add(lv.nextInt(20) - 10, lv.nextInt(6) - 3, lv.nextInt(20) - 10);
            if (!SeekSkyTask.isSkyVisible(world, entity, lv3)) continue;
            return Vec3d.ofBottomCenter(lv3);
        }
        return null;
    }

    public static boolean isSkyVisible(ServerWorld world, LivingEntity entity, BlockPos pos) {
        return world.isSkyVisible(pos) && (double)world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() <= entity.getY();
    }
}

