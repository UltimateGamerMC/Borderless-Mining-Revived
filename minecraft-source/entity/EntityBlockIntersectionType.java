/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/EntityBlockIntersectionType;method_74566()[Lnet/minecraft/entity/EntityBlockIntersectionType;
 *   Lnet/minecraft/entity/EntityBlockIntersectionType;values()[Lnet/minecraft/entity/EntityBlockIntersectionType;
 */
package net.minecraft.entity;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.function.ValueLists;

public enum EntityBlockIntersectionType {
    IN_BLOCK(0, 0x6000FF00),
    IN_FLUID(1, 0x600000FF),
    IN_AIR(2, 0x60333333);

    private static final IntFunction<EntityBlockIntersectionType> BY_ID;
    public static final PacketCodec<ByteBuf, EntityBlockIntersectionType> PACKET_CODEC;
    private final int id;
    private final int color;

    private EntityBlockIntersectionType(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(type -> type.id, EntityBlockIntersectionType.values(), ValueLists.OutOfBoundsHandling.ZERO);
        PACKET_CODEC = PacketCodecs.indexed(BY_ID, type -> type.id);
    }
}

