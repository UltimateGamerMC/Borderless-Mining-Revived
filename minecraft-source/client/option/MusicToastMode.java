/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/MusicToastMode;method_76608()[Lnet/minecraft/client/option/MusicToastMode;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum MusicToastMode implements StringIdentifiable
{
    NEVER("never", "options.musicToast.never"),
    PAUSE("pause", "options.musicToast.pauseMenu"),
    PAUSE_AND_TOAST("pause_and_toast", "options.musicToast.pauseMenuAndToast");

    public static final Codec<MusicToastMode> CODEC;
    private final String id;
    private final Text text;
    private final Text tooltipText;

    private MusicToastMode(String id, String translationKey) {
        this.id = id;
        this.text = Text.translatable(translationKey);
        this.tooltipText = Text.translatable(translationKey + ".tooltip");
    }

    public Text getText() {
        return this.text;
    }

    public Text getTooltipText() {
        return this.tooltipText;
    }

    @Override
    public String asString() {
        return this.id;
    }

    public boolean canShow() {
        return this != NEVER;
    }

    public boolean canShowAsToast() {
        return this == PAUSE_AND_TOAST;
    }

    static {
        CODEC = StringIdentifiable.createCodec(MusicToastMode::values);
    }
}

