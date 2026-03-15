/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/hud/debug/DebugProfileType;method_72783()[Lnet/minecraft/client/gui/hud/debug/DebugProfileType;
 */
package net.minecraft.client.gui.hud.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum DebugProfileType implements StringIdentifiable
{
    DEFAULT("default", "debug.options.profile.default"),
    PERFORMANCE("performance", "debug.options.profile.performance");

    public static final StringIdentifiable.EnumCodec<DebugProfileType> CODEC;
    private final String id;
    private final String translationKey;

    private DebugProfileType(String id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(DebugProfileType::values);
    }
}

