/*
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/FuzzyPositions;guessBest(Ljava/util/function/Supplier;Ljava/util/function/ToDoubleFunction;)Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.entity.ai;

import com.google.common.annotations.VisibleForTesting;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

public class FuzzyPositions {
    private static final int GAUSS_RANGE = 10;

    public static BlockPos localFuzz(Random random, int horizontalRange, int verticalRange) {
        int k = random.nextInt(2 * horizontalRange + 1) - horizontalRange;
        int l = random.nextInt(2 * verticalRange + 1) - verticalRange;
        int m = random.nextInt(2 * horizontalRange + 1) - horizontalRange;
        return new BlockPos(k, l, m);
    }

    public static @Nullable BlockPos localFuzz(Random random, double minHorizontalRange, double maxHorizontalRange, int verticalRange, int startHeight, double directionX, double directionZ, double angleRange) {
        double k = MathHelper.atan2(directionZ, directionX) - 1.5707963705062866;
        double l = k + (double)(2.0f * random.nextFloat() - 1.0f) * angleRange;
        double m = MathHelper.lerp(Math.sqrt(random.nextDouble()), minHorizontalRange, maxHorizontalRange) * (double)MathHelper.SQUARE_ROOT_OF_TWO;
        double n = -m * Math.sin(l);
        double o = m * Math.cos(l);
        if (Math.abs(n) > maxHorizontalRange || Math.abs(o) > maxHorizontalRange) {
            return null;
        }
        int p = random.nextInt(2 * verticalRange + 1) - verticalRange + startHeight;
        return BlockPos.ofFloored(n, p, o);
    }

    @VisibleForTesting
    public static BlockPos upWhile(BlockPos pos, int maxY, Predicate<BlockPos> condition) {
        if (condition.test(pos)) {
            BlockPos.Mutable lv = pos.mutableCopy().move(Direction.UP);
            while (lv.getY() <= maxY && condition.test(lv)) {
                lv.move(Direction.UP);
            }
            return lv.toImmutable();
        }
        return pos;
    }

    @VisibleForTesting
    public static BlockPos upWhile(BlockPos pos, int extraAbove, int max, Predicate<BlockPos> condition) {
        if (extraAbove < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + extraAbove + ", expected >= 0");
        }
        if (condition.test(pos)) {
            BlockPos.Mutable lv = pos.mutableCopy().move(Direction.UP);
            while (lv.getY() <= max && condition.test(lv)) {
                lv.move(Direction.UP);
            }
            int k = lv.getY();
            while (lv.getY() <= max && lv.getY() - k < extraAbove) {
                lv.move(Direction.UP);
                if (!condition.test(lv)) continue;
                lv.move(Direction.DOWN);
                break;
            }
            return lv.toImmutable();
        }
        return pos;
    }

    public static @Nullable Vec3d guessBestPathTarget(PathAwareEntity entity, Supplier<@Nullable BlockPos> factory) {
        return FuzzyPositions.guessBest(factory, entity::getPathfindingFavor);
    }

    public static @Nullable Vec3d guessBest(Supplier<@Nullable BlockPos> factory, ToDoubleFunction<BlockPos> scorer) {
        double d = Double.NEGATIVE_INFINITY;
        BlockPos lv = null;
        for (int i = 0; i < 10; ++i) {
            double e;
            BlockPos lv2 = factory.get();
            if (lv2 == null || !((e = scorer.applyAsDouble(lv2)) > d)) continue;
            d = e;
            lv = lv2;
        }
        return lv != null ? Vec3d.ofBottomCenter(lv) : null;
    }

    public static BlockPos towardTarget(PathAwareEntity entity, double horizontalRange, Random random, BlockPos fuzz) {
        double e = fuzz.getX();
        double f = fuzz.getZ();
        if (entity.hasPositionTarget() && horizontalRange > 1.0) {
            BlockPos lv = entity.getPositionTarget();
            e = entity.getX() > (double)lv.getX() ? (e -= random.nextDouble() * horizontalRange / 2.0) : (e += random.nextDouble() * horizontalRange / 2.0);
            f = entity.getZ() > (double)lv.getZ() ? (f -= random.nextDouble() * horizontalRange / 2.0) : (f += random.nextDouble() * horizontalRange / 2.0);
        }
        return BlockPos.ofFloored(e + entity.getX(), (double)fuzz.getY() + entity.getY(), f + entity.getZ());
    }
}

