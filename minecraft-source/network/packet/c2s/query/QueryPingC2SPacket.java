/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerQueryPingPacketListener;onQueryPing(Lnet/minecraft/network/packet/c2s/query/QueryPingC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/query/QueryPingC2SPacket;apply(Lnet/minecraft/network/listener/ServerQueryPingPacketListener;)V
 */
package net.minecraft.network.packet.c2s.query;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerQueryPingPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PingPackets;

public class QueryPingC2SPacket
implements Packet<ServerQueryPingPacketListener> {
    public static final PacketCodec<ByteBuf, QueryPingC2SPacket> CODEC = Packet.createCodec(QueryPingC2SPacket::write, QueryPingC2SPacket::new);
    private final long startTime;

    public QueryPingC2SPacket(long startTime) {
        this.startTime = startTime;
    }

    private QueryPingC2SPacket(ByteBuf buf) {
        this.startTime = buf.readLong();
    }

    private void write(ByteBuf buf) {
        buf.writeLong(this.startTime);
    }

    @Override
    public PacketType<QueryPingC2SPacket> getPacketType() {
        return PingPackets.PING_REQUEST;
    }

    @Override
    public void apply(ServerQueryPingPacketListener arg) {
        arg.onQueryPing(this);
    }

    public long getStartTime() {
        return this.startTime;
    }
}

