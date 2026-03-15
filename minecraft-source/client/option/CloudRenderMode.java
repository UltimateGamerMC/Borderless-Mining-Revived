/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/CloudRenderMode;method_36860()[Lnet/minecraft/client/option/CloudRenderMode;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum CloudRenderMode implements StringIdentifiable
{
    OFF("false", "options.off"),
    FAST("fast", "options.clouds.fast"),
    FANCY("true", "options.clouds.fancy");

    public static final Codec<CloudRenderMode> CODEC;
    private final String serializedId;
    private final Text text;

    private CloudRenderMode(String serializedId, String translationKey) {
        this.serializedId = serializedId;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    @Override
    public String asString() {
        return this.serializedId;
    }

    static {
        CODEC = StringIdentifiable.createCodec(CloudRenderMode::values);
    }
}

