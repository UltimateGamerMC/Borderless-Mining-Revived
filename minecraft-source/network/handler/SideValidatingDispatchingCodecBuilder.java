/*
 * External method calls:
 *   Lnet/minecraft/network/handler/PacketCodecDispatcher;builder(Ljava/util/function/Function;)Lnet/minecraft/network/handler/PacketCodecDispatcher$Builder;
 *   Lnet/minecraft/network/packet/PacketType;side()Lnet/minecraft/network/NetworkSide;
 *   Lnet/minecraft/network/NetworkSide;name()Ljava/lang/String;
 *   Lnet/minecraft/network/handler/PacketCodecDispatcher$Builder;build()Lnet/minecraft/network/handler/PacketCodecDispatcher;
 */
package net.minecraft.network.handler;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.handler.PacketCodecDispatcher;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class SideValidatingDispatchingCodecBuilder<B extends ByteBuf, L extends PacketListener> {
    private final PacketCodecDispatcher.Builder<B, Packet<? super L>, PacketType<? extends Packet<? super L>>> backingBuilder = PacketCodecDispatcher.builder(Packet::getPacketType);
    private final NetworkSide side;

    public SideValidatingDispatchingCodecBuilder(NetworkSide side) {
        this.side = side;
    }

    public <T extends Packet<? super L>> SideValidatingDispatchingCodecBuilder<B, L> add(PacketType<T> id, PacketCodec<? super B, T> codec) {
        if (id.side() != this.side) {
            throw new IllegalArgumentException("Invalid packet flow for packet " + String.valueOf(id) + ", expected " + this.side.name());
        }
        this.backingBuilder.add(id, codec);
        return this;
    }

    public PacketCodec<B, Packet<? super L>> build() {
        return this.backingBuilder.build();
    }
}

