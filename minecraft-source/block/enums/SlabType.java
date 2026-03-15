/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/SlabType;method_36735()[Lnet/minecraft/block/enums/SlabType;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum SlabType implements StringIdentifiable
{
    TOP("top"),
    BOTTOM("bottom"),
    DOUBLE("double");

    private final String name;

    private SlabType(String name) {
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

