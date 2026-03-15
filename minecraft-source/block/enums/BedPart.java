/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/BedPart;method_36722()[Lnet/minecraft/block/enums/BedPart;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum BedPart implements StringIdentifiable
{
    HEAD("head"),
    FOOT("foot");

    private final String name;

    private BedPart(String name) {
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

