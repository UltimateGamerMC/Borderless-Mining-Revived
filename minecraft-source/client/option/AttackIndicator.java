/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/AttackIndicator;method_36858()[Lnet/minecraft/client/option/AttackIndicator;
 *   Lnet/minecraft/client/option/AttackIndicator;values()[Lnet/minecraft/client/option/AttackIndicator;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

@Environment(value=EnvType.CLIENT)
public enum AttackIndicator {
    OFF(0, "options.off"),
    CROSSHAIR(1, "options.attack.crosshair"),
    HOTBAR(2, "options.attack.hotbar");

    private static final IntFunction<AttackIndicator> BY_ID;
    public static final Codec<AttackIndicator> CODEC;
    private final int id;
    private final Text text;

    private AttackIndicator(int id, String translationKey) {
        this.id = id;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(attackIndicator -> attackIndicator.id, AttackIndicator.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(BY_ID::apply, attackIndicator -> attackIndicator.id);
    }
}

