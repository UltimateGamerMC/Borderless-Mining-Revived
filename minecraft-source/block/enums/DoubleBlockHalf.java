/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/DoubleBlockHalf;asString()Ljava/lang/String;
 *   Lnet/minecraft/block/enums/DoubleBlockHalf;method_36727()[Lnet/minecraft/block/enums/DoubleBlockHalf;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum DoubleBlockHalf implements StringIdentifiable
{
    UPPER(Direction.DOWN),
    LOWER(Direction.UP);

    private final Direction oppositeDirection;

    private DoubleBlockHalf(Direction oppositeDirection) {
        this.oppositeDirection = oppositeDirection;
    }

    public Direction getOppositeDirection() {
        return this.oppositeDirection;
    }

    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        return this == UPPER ? "upper" : "lower";
    }

    public DoubleBlockHalf getOtherHalf() {
        return this == UPPER ? LOWER : UPPER;
    }
}

