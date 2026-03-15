/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/ChunkBuilderMode;method_38526()[Lnet/minecraft/client/render/ChunkBuilderMode;
 *   Lnet/minecraft/client/render/ChunkBuilderMode;values()[Lnet/minecraft/client/render/ChunkBuilderMode;
 */
package net.minecraft.client.render;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

@Environment(value=EnvType.CLIENT)
public enum ChunkBuilderMode {
    NONE(0, "options.prioritizeChunkUpdates.none"),
    PLAYER_AFFECTED(1, "options.prioritizeChunkUpdates.byPlayer"),
    NEARBY(2, "options.prioritizeChunkUpdates.nearby");

    private static final IntFunction<ChunkBuilderMode> BY_ID;
    public static final Codec<ChunkBuilderMode> CODEC;
    private final int id;
    private final Text text;

    private ChunkBuilderMode(int id, String name) {
        this.id = id;
        this.text = Text.translatable(name);
    }

    public Text getText() {
        return this.text;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(arg -> arg.id, ChunkBuilderMode.values(), ValueLists.OutOfBoundsHandling.WRAP);
        CODEC = Codec.INT.xmap(BY_ID::apply, mode -> mode.id);
    }
}

