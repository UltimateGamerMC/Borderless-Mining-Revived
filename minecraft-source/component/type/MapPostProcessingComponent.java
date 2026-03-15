/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/component/type/MapPostProcessingComponent;method_57506()[Lnet/minecraft/component/type/MapPostProcessingComponent;
 *   Lnet/minecraft/component/type/MapPostProcessingComponent;values()[Lnet/minecraft/component/type/MapPostProcessingComponent;
 */
package net.minecraft.component.type;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.function.ValueLists;

public enum MapPostProcessingComponent {
    LOCK(0),
    SCALE(1);

    public static final IntFunction<MapPostProcessingComponent> ID_TO_VALUE;
    public static final PacketCodec<ByteBuf, MapPostProcessingComponent> PACKET_CODEC;
    private final int id;

    private MapPostProcessingComponent(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    static {
        ID_TO_VALUE = ValueLists.createIndexToValueFunction(MapPostProcessingComponent::getId, MapPostProcessingComponent.values(), ValueLists.OutOfBoundsHandling.ZERO);
        PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, MapPostProcessingComponent::getId);
    }
}

