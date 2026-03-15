/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/Unit;method_36588()[Lnet/minecraft/util/Unit;
 */
package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum Unit {
    INSTANCE;

    public static final Codec<Unit> CODEC;
    public static final PacketCodec<ByteBuf, Unit> PACKET_CODEC;

    static {
        CODEC = MapCodec.unitCodec(INSTANCE);
        PACKET_CODEC = PacketCodec.unit(INSTANCE);
    }
}

