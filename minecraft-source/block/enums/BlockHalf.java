/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/BlockHalf;method_36729()[Lnet/minecraft/block/enums/BlockHalf;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum BlockHalf implements StringIdentifiable
{
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private BlockHalf(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}

