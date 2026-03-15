/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/message/ChatVisibility;method_36660()[Lnet/minecraft/network/message/ChatVisibility;
 *   Lnet/minecraft/network/message/ChatVisibility;values()[Lnet/minecraft/network/message/ChatVisibility;
 */
package net.minecraft.network.message;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

public enum ChatVisibility {
    FULL(0, "options.chat.visibility.full"),
    SYSTEM(1, "options.chat.visibility.system"),
    HIDDEN(2, "options.chat.visibility.hidden");

    private static final IntFunction<ChatVisibility> BY_ID;
    public static final Codec<ChatVisibility> CODEC;
    private final int id;
    private final Text text;

    private ChatVisibility(int id, String translationKey) {
        this.id = id;
        this.text = Text.translatable(translationKey);
    }

    public Text getText() {
        return this.text;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(arg -> arg.id, ChatVisibility.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(BY_ID::apply, visibility -> visibility.id);
    }
}

