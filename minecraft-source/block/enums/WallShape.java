/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/WallShape;asString()Ljava/lang/String;
 *   Lnet/minecraft/block/enums/WallShape;method_36739()[Lnet/minecraft/block/enums/WallShape;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum WallShape implements StringIdentifiable
{
    NONE("none"),
    LOW("low"),
    TALL("tall");

    private final String name;

    private WallShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        return this.name;
    }
}

