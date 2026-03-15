/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/FuzzyPositions;guessBestPathTarget(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/util/function/Supplier;)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;towardTarget(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;localFuzz(Lnet/minecraft/util/math/random/Random;DDIIDDD)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;localFuzz(Lnet/minecraft/util/math/random/Random;II)Lnet/minecraft/util/math/BlockPos;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/NoPenaltyTargeting;tryMake(Lnet/minecraft/entity/mob/PathAwareEntity;IZLnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class NoPenaltyTargeting {
    public static @Nullable Vec3d find(PathAwareEntity entity, int horizontalRange, int verticalRange) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> {
            BlockPos lv = FuzzyPositions.localFuzz(entity.getRandom(), horizontalRange, verticalRange);
            return NoPenaltyTargeting.tryMake(entity, horizontalRange, bl, lv);
        });
    }

    public static @Nullable Vec3d findTo(PathAwareEntity entity, int horizontalRange, int verticalRange, Vec3d end, double angleRange) {
        Vec3d lv = end.subtract(entity.getX(), entity.getY(), entity.getZ());
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> {
            BlockPos lv = FuzzyPositions.localFuzz(entity.getRandom(), 0.0, horizontalRange, verticalRange, 0, arg2.x, arg2.z, angleRange);
            if (lv == null) {
                return null;
            }
            return NoPenaltyTargeting.tryMake(entity, horizontalRange, bl, lv);
        });
    }

    public static @Nullable Vec3d findFrom(PathAwareEntity entity, int horizontalRange, int verticalRange, Vec3d start) {
        Vec3d lv = entity.getEntityPos().subtract(start);
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> {
            BlockPos lv = FuzzyPositions.localFuzz(entity.getRandom(), 0.0, horizontalRange, verticalRange, 0, arg2.x, arg2.z, 1.5707963705062866);
            if (lv == null) {
                return null;
            }
            return NoPenaltyTargeting.tryMake(entity, horizontalRange, bl, lv);
        });
    }

    private static @Nullable BlockPos tryMake(PathAwareEntity entity, int horizontalRange, boolean posTargetInRange, BlockPos fuzz) {
        BlockPos lv = FuzzyPositions.towardTarget(entity, horizontalRange, entity.getRandom(), fuzz);
        if (NavigationConditions.isHeightInvalid(lv, entity) || NavigationConditions.isPositionTargetOutOfWalkRange(posTargetInRange, entity, lv) || NavigationConditions.isInvalidPosition(entity.getNavigation(), lv) || NavigationConditions.hasPathfindingPenalty(entity, lv)) {
            return null;
        }
        return lv;
    }
}

