/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/Attachment;method_36723()[Lnet/minecraft/block/enums/Attachment;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum Attachment implements StringIdentifiable
{
    FLOOR("floor"),
    CEILING("ceiling"),
    SINGLE_WALL("single_wall"),
    DOUBLE_WALL("double_wall");

    private final String name;

    private Attachment(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}

