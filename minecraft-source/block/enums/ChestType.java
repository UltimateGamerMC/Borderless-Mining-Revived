/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/ChestType;method_36724()[Lnet/minecraft/block/enums/ChestType;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum ChestType implements StringIdentifiable
{
    SINGLE("single"),
    LEFT("left"),
    RIGHT("right");

    private final String name;

    private ChestType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public ChestType getOpposite() {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> SINGLE;
            case 1 -> RIGHT;
            case 2 -> LEFT;
        };
    }
}

