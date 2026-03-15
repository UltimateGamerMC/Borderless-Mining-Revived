/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/slot/SlotActionType;method_36673()[Lnet/minecraft/screen/slot/SlotActionType;
 *   Lnet/minecraft/screen/slot/SlotActionType;values()[Lnet/minecraft/screen/slot/SlotActionType;
 */
package net.minecraft.screen.slot;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.function.ValueLists;

public enum SlotActionType {
    PICKUP(0),
    QUICK_MOVE(1),
    SWAP(2),
    CLONE(3),
    THROW(4),
    QUICK_CRAFT(5),
    PICKUP_ALL(6);

    private static final IntFunction<SlotActionType> INDEX_MAPPER;
    public static final PacketCodec<ByteBuf, SlotActionType> PACKET_CODEC;
    private final int index;

    private SlotActionType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    static {
        INDEX_MAPPER = ValueLists.createIndexToValueFunction(SlotActionType::getIndex, SlotActionType.values(), ValueLists.OutOfBoundsHandling.ZERO);
        PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, SlotActionType::getIndex);
    }
}

