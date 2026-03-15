/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPingResultPacketListener;onPingResult(Lnet/minecraft/network/packet/s2c/query/PingResultS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/query/PingResultS2CPacket;apply(Lnet/minecraft/network/listener/ClientPingResultPacketListener;)V
 */
package net.minecraft.network.packet.s2c.query;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPingResultPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PingPackets;

public record PingResultS2CPacket(long startTime) implements Packet<ClientPingResultPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PingResultS2CPacket> CODEC = Packet.createCodec(PingResultS2CPacket::write, PingResultS2CPacket::new);

    private PingResultS2CPacket(PacketByteBuf buf) {
        this(buf.readLong());
    }

    private void write(PacketByteBuf buf) {
        buf.writeLong(this.startTime);
    }

    @Override
    public PacketType<PingResultS2CPacket> getPacketType() {
        return PingPackets.PONG_RESPONSE;
    }

    @Override
    public void apply(ClientPingResultPacketListener arg) {
        arg.onPingResult(this);
    }
}

