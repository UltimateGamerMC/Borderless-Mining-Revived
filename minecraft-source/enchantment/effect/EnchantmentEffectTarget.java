/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/enchantment/effect/EnchantmentEffectTarget;method_60182()[Lnet/minecraft/enchantment/effect/EnchantmentEffectTarget;
 */
package net.minecraft.enchantment.effect;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum EnchantmentEffectTarget implements StringIdentifiable
{
    ATTACKER("attacker"),
    DAMAGING_ENTITY("damaging_entity"),
    VICTIM("victim");

    public static final Codec<EnchantmentEffectTarget> CODEC;
    private final String id;

    private EnchantmentEffectTarget(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(EnchantmentEffectTarget::values);
    }
}

