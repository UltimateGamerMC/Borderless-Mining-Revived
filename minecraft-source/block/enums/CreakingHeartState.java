/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/CreakingHeartState;method_66479()[Lnet/minecraft/block/enums/CreakingHeartState;
 */
package net.minecraft.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum CreakingHeartState implements StringIdentifiable
{
    UPROOTED("uprooted"),
    DORMANT("dormant"),
    AWAKE("awake");

    private final String id;

    private CreakingHeartState(String id) {
        this.id = id;
    }

    public String toString() {
        return this.id;
    }

    @Override
    public String asString() {
        return this.id;
    }
}

