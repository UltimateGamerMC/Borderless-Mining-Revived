/*
 * Internal private/static methods:
 *   Lnet/minecraft/entity/effect/StatusEffectCategory;method_36600()[Lnet/minecraft/entity/effect/StatusEffectCategory;
 */
package net.minecraft.entity.effect;

import net.minecraft.util.Formatting;

public enum StatusEffectCategory {
    BENEFICIAL(Formatting.BLUE),
    HARMFUL(Formatting.RED),
    NEUTRAL(Formatting.BLUE);

    private final Formatting formatting;

    private StatusEffectCategory(Formatting format) {
        this.formatting = format;
    }

    public Formatting getFormatting() {
        return this.formatting;
    }
}

