/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/FuzzyPositions;guessBestPathTarget(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/util/function/Supplier;)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;localFuzz(Lnet/minecraft/util/math/random/Random;DDIIDDD)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;towardTarget(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/ai/FuzzyPositions;upWhile(Lnet/minecraft/util/math/BlockPos;ILjava/util/function/Predicate;)Lnet/minecraft/util/math/BlockPos;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/NoPenaltySolidTargeting;tryMake(Lnet/minecraft/entity/mob/PathAwareEntity;IIIDDDZ)Lnet/minecraft/util/math/BlockPos;
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class NoPenaltySolidTargeting {
    public static @Nullable Vec3d find(PathAwareEntity entity, int horizontalRange, int verticalRange, int startHeight, double directionX, double directionZ, double rangeAngle) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> NoPenaltySolidTargeting.tryMake(entity, horizontalRange, verticalRange, startHeight, directionX, directionZ, rangeAngle, bl));
    }

    public static @Nullable BlockPos tryMake(PathAwareEntity entity, int horizontalRange, int verticalRange, int startHeight, double directionX, double directionZ, double rangeAngle, boolean posTargetInRange) {
        BlockPos lv = FuzzyPositions.localFuzz(entity.getRandom(), 0.0, horizontalRange, verticalRange, startHeight, directionX, directionZ, rangeAngle);
        if (lv == null) {
            return null;
        }
        BlockPos lv2 = FuzzyPositions.towardTarget(entity, horizontalRange, entity.getRandom(), lv);
        if (NavigationConditions.isHeightInvalid(lv2, entity) || NavigationConditions.isPositionTargetOutOfWalkRange(posTargetInRange, entity, lv2)) {
            return null;
        }
        if (NavigationConditions.hasPathfindingPenalty(entity, lv2 = FuzzyPositions.upWhile(lv2, entity.getEntityWorld().getTopYInclusive(), pos -> NavigationConditions.isSolidAt(entity, pos)))) {
            return null;
        }
        return lv2;
    }
}

