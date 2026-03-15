/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/BlockFace;method_36720()[Lnet/minecraft/block/enums/BlockFace;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum BlockFace implements StringIdentifiable
{
    FLOOR("floor"),
    WALL("wall"),
    CEILING("ceiling");

    private final String name;

    private BlockFace(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}

