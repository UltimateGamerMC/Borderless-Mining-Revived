/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/particle/ParticlesMode;method_36865()[Lnet/minecraft/particle/ParticlesMode;
 *   Lnet/minecraft/particle/ParticlesMode;values()[Lnet/minecraft/particle/ParticlesMode;
 */
package net.minecraft.particle;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

public enum ParticlesMode {
    ALL(0, "options.particles.all"),
    DECREASED(1, "options.particles.decreased"),
    MINIMAL(2, "options.particles.minimal");

    private static final IntFunction<ParticlesMode> BY_ID;
    public static final Codec<ParticlesMode> CODEC;
    private final int id;
    private final Text text;

    private ParticlesMode(int id, String translationKey) {
        this.id = id;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(arg -> arg.id, ParticlesMode.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(BY_ID::apply, mode -> mode.id);
    }
}

