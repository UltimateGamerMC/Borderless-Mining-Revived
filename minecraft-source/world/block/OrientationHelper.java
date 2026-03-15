/*
 * External method calls:
 *   Lnet/minecraft/world/block/WireOrientation;random(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/world/block/WireOrientation;
 *   Lnet/minecraft/world/block/WireOrientation;withSideBias(Lnet/minecraft/world/block/WireOrientation$SideBias;)Lnet/minecraft/world/block/WireOrientation;
 *   Lnet/minecraft/world/block/WireOrientation;withUp(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/world/block/WireOrientation;
 *   Lnet/minecraft/world/block/WireOrientation;withFront(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/world/block/WireOrientation;
 */
package net.minecraft.world.block;

import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;

public class OrientationHelper {
    public static @Nullable WireOrientation getEmissionOrientation(World world, @Nullable Direction up, @Nullable Direction front) {
        if (world.getEnabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
            WireOrientation lv = WireOrientation.random(world.random).withSideBias(WireOrientation.SideBias.LEFT);
            if (front != null) {
                lv = lv.withUp(front);
            }
            if (up != null) {
                lv = lv.withFront(up);
            }
            return lv;
        }
        return null;
    }

    public static @Nullable WireOrientation withFrontNullable(@Nullable WireOrientation orientation, Direction direction) {
        return orientation == null ? null : orientation.withFront(direction);
    }
}

