/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/DoorHinge;asString()Ljava/lang/String;
 *   Lnet/minecraft/block/enums/DoorHinge;method_36726()[Lnet/minecraft/block/enums/DoorHinge;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum DoorHinge implements StringIdentifiable
{
    LEFT,
    RIGHT;


    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        return this == LEFT ? "left" : "right";
    }
}

