/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/enums/TestBlockMode;method_66785()[Lnet/minecraft/block/enums/TestBlockMode;
 *   Lnet/minecraft/block/enums/TestBlockMode;values()[Lnet/minecraft/block/enums/TestBlockMode;
 */
package net.minecraft.block.enums;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum TestBlockMode implements StringIdentifiable
{
    START(0, "start"),
    LOG(1, "log"),
    FAIL(2, "fail"),
    ACCEPT(3, "accept");

    private static final IntFunction<TestBlockMode> INDEX_MAPPER;
    public static final Codec<TestBlockMode> CODEC;
    public static final PacketCodec<ByteBuf, TestBlockMode> PACKET_CODEC;
    private final int index;
    private final String id;
    private final Text name;
    private final Text info;

    private TestBlockMode(int index, String id) {
        this.index = index;
        this.id = id;
        this.name = Text.translatable("test_block.mode." + id);
        this.info = Text.translatable("test_block.mode_info." + id);
    }

    @Override
    public String asString() {
        return this.id;
    }

    public Text getName() {
        return this.name;
    }

    public Text getInfo() {
        return this.info;
    }

    static {
        INDEX_MAPPER = ValueLists.createIndexToValueFunction(mode -> mode.index, TestBlockMode.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(TestBlockMode::values);
        PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, mode -> mode.index);
    }
}

