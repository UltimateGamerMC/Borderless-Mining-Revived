/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/NarratorMode;method_36864()[Lnet/minecraft/client/option/NarratorMode;
 *   Lnet/minecraft/client/option/NarratorMode;values()[Lnet/minecraft/client/option/NarratorMode;
 */
package net.minecraft.client.option;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

@Environment(value=EnvType.CLIENT)
public enum NarratorMode {
    OFF(0, "options.narrator.off"),
    ALL(1, "options.narrator.all"),
    CHAT(2, "options.narrator.chat"),
    SYSTEM(3, "options.narrator.system");

    private static final IntFunction<NarratorMode> BY_ID;
    public static final Codec<NarratorMode> CODEC;
    private final int id;
    private final Text name;

    private NarratorMode(int id, String name) {
        this.id = id;
        this.name = Text.translatable(name);
    }

    public int getId() {
        return this.id;
    }

    public Text getName() {
        return this.name;
    }

    public static NarratorMode byId(int id) {
        return BY_ID.apply(id);
    }

    public boolean shouldNarrateChat() {
        return this == ALL || this == CHAT;
    }

    public boolean shouldNarrateSystem() {
        return this == ALL || this == SYSTEM;
    }

    public boolean shouldNarrate() {
        return this == ALL || this == SYSTEM || this == CHAT;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(NarratorMode::getId, NarratorMode.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(NarratorMode::byId, NarratorMode::getId);
    }
}

