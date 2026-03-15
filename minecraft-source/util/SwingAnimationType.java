/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/SwingAnimationType;method_75227()[Lnet/minecraft/util/SwingAnimationType;
 *   Lnet/minecraft/util/SwingAnimationType;values()[Lnet/minecraft/util/SwingAnimationType;
 */
package net.minecraft.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum SwingAnimationType implements StringIdentifiable
{
    NONE(0, "none"),
    WHACK(1, "whack"),
    STAB(2, "stab");

    private static final IntFunction<SwingAnimationType> BY_PACKET_ID;
    public static final Codec<SwingAnimationType> CODEC;
    public static final PacketCodec<ByteBuf, SwingAnimationType> PACKET_CODEC;
    private final int packetId;
    private final String name;

    private SwingAnimationType(int packetId, String name) {
        this.packetId = packetId;
        this.name = name;
    }

    public int getPacketId() {
        return this.packetId;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        BY_PACKET_ID = ValueLists.createIndexToValueFunction(SwingAnimationType::getPacketId, SwingAnimationType.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(SwingAnimationType::values);
        PACKET_CODEC = PacketCodecs.indexed(BY_PACKET_ID, SwingAnimationType::getPacketId);
    }
}

