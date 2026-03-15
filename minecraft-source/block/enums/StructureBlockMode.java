/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/dynamic/Codecs;enumByName(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/StructureBlockMode;method_36737()[Lnet/minecraft/block/enums/StructureBlockMode;
 */
package net.minecraft.block.enums;

import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;

public enum StructureBlockMode implements StringIdentifiable
{
    SAVE("save"),
    LOAD("load"),
    CORNER("corner"),
    DATA("data");

    @Deprecated
    public static final Codec<StructureBlockMode> CODEC;
    private final String name;
    private final Text text;

    private StructureBlockMode(String name) {
        this.name = name;
        this.text = Text.translatable("structure_block.mode_info." + name);
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text asText() {
        return this.text;
    }

    static {
        CODEC = Codecs.enumByName(StructureBlockMode::valueOf);
    }
}

