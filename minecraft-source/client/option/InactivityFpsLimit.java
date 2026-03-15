/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/InactivityFpsLimit;method_61961()[Lnet/minecraft/client/option/InactivityFpsLimit;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum InactivityFpsLimit implements StringIdentifiable
{
    MINIMIZED("minimized", "options.inactivityFpsLimit.minimized"),
    AFK("afk", "options.inactivityFpsLimit.afk");

    public static final Codec<InactivityFpsLimit> CODEC;
    private final String name;
    private final Text text;

    private InactivityFpsLimit(String name, String translationKey) {
        this.name = name;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createCodec(InactivityFpsLimit::values);
    }
}

