/*
 * Internal private/static methods:
 *   Lnet/minecraft/world/rule/GameRuleType;method_73877()[Lnet/minecraft/world/rule/GameRuleType;
 */
package net.minecraft.world.rule;

import net.minecraft.util.StringIdentifiable;

public enum GameRuleType implements StringIdentifiable
{
    INT("integer"),
    BOOL("boolean");

    private final String name;

    private GameRuleType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}

