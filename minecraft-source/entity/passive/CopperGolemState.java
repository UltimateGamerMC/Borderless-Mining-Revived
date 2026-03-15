/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/CopperGolemState;method_72495()[Lnet/minecraft/entity/passive/CopperGolemState;
 *   Lnet/minecraft/entity/passive/CopperGolemState;values()[Lnet/minecraft/entity/passive/CopperGolemState;
 */
package net.minecraft.entity.passive;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum CopperGolemState implements StringIdentifiable
{
    IDLE("idle", 0),
    GETTING_ITEM("getting_item", 1),
    GETTING_NO_ITEM("getting_no_item", 2),
    DROPPING_ITEM("dropping_item", 3),
    DROPPING_NO_ITEM("dropping_no_item", 4);

    public static final Codec<CopperGolemState> CODEC;
    private static final IntFunction<CopperGolemState> INDEX_MAPPER;
    public static final PacketCodec<ByteBuf, CopperGolemState> PACKET_CODEC;
    private final String id;
    private final int index;

    private CopperGolemState(String id, int index) {
        this.id = id;
        this.index = index;
    }

    @Override
    public String asString() {
        return this.id;
    }

    private int getIndex() {
        return this.index;
    }

    static {
        CODEC = StringIdentifiable.createCodec(CopperGolemState::values);
        INDEX_MAPPER = ValueLists.createIndexToValueFunction(CopperGolemState::getIndex, CopperGolemState.values(), ValueLists.OutOfBoundsHandling.ZERO);
        PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, CopperGolemState::getIndex);
    }
}

