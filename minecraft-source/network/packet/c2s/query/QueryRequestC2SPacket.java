/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerQueryPacketListener;onRequest(Lnet/minecraft/network/packet/c2s/query/QueryRequestC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/query/QueryRequestC2SPacket;apply(Lnet/minecraft/network/listener/ServerQueryPacketListener;)V
 */
package net.minecraft.network.packet.c2s.query;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerQueryPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.StatusPackets;

public class QueryRequestC2SPacket
implements Packet<ServerQueryPacketListener> {
    public static final QueryRequestC2SPacket INSTANCE = new QueryRequestC2SPacket();
    public static final PacketCodec<ByteBuf, QueryRequestC2SPacket> CODEC = PacketCodec.unit(INSTANCE);

    private QueryRequestC2SPacket() {
    }

    @Override
    public PacketType<QueryRequestC2SPacket> getPacketType() {
        return StatusPackets.STATUS_REQUEST;
    }

    @Override
    public void apply(ServerQueryPacketListener arg) {
        arg.onRequest(this);
    }
}

