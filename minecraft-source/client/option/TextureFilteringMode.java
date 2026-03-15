/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/TextureFilteringMode;method_76753()[Lnet/minecraft/client/option/TextureFilteringMode;
 *   Lnet/minecraft/client/option/TextureFilteringMode;values()[Lnet/minecraft/client/option/TextureFilteringMode;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

@Environment(value=EnvType.CLIENT)
public enum TextureFilteringMode {
    NONE(0, "options.textureFiltering.none"),
    RGSS(1, "options.textureFiltering.rgss"),
    ANISOTROPIC(2, "options.textureFiltering.anisotropic");

    private static final IntFunction<TextureFilteringMode> BY_ID;
    public static final Codec<TextureFilteringMode> CODEC;
    private final int id;
    private final Text text;

    private TextureFilteringMode(int id, String translationKey) {
        this.id = id;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(mode -> mode.id, TextureFilteringMode.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(BY_ID::apply, mode -> mode.id);
    }
}

