/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/dialog/AfterAction;method_72065()[Lnet/minecraft/dialog/AfterAction;
 *   Lnet/minecraft/dialog/AfterAction;values()[Lnet/minecraft/dialog/AfterAction;
 */
package net.minecraft.dialog;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum AfterAction implements StringIdentifiable
{
    CLOSE(0, "close"),
    NONE(1, "none"),
    WAIT_FOR_RESPONSE(2, "wait_for_response");

    public static final IntFunction<AfterAction> INDEX_MAPPER;
    public static final StringIdentifiable.EnumCodec<AfterAction> CODEC;
    public static final PacketCodec<ByteBuf, AfterAction> PACKET_CODEC;
    private final int index;
    private final String id;

    private AfterAction(int index, String id) {
        this.index = index;
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }

    public boolean canUnpause() {
        return this == CLOSE || this == WAIT_FOR_RESPONSE;
    }

    static {
        INDEX_MAPPER = ValueLists.createIndexToValueFunction(afterAction -> afterAction.index, AfterAction.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(AfterAction::values);
        PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, afterAction -> afterAction.index);
    }
}

