/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;
 *   Lnet/minecraft/network/packet/c2s/handshake/ConnectionIntent;byId(I)Lnet/minecraft/network/packet/c2s/handshake/ConnectionIntent;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeShort(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerHandshakePacketListener;onHandshake(Lnet/minecraft/network/packet/c2s/handshake/HandshakeC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/handshake/HandshakeC2SPacket;apply(Lnet/minecraft/network/listener/ServerHandshakePacketListener;)V
 */
package net.minecraft.network.packet.c2s.handshake;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerHandshakePacketListener;
import net.minecraft.network.packet.HandshakePackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;

public record HandshakeC2SPacket(int protocolVersion, String address, int port, ConnectionIntent intendedState) implements Packet<ServerHandshakePacketListener>
{
    public static final PacketCodec<PacketByteBuf, HandshakeC2SPacket> CODEC = Packet.createCodec(HandshakeC2SPacket::write, HandshakeC2SPacket::new);
    private static final int MAX_ADDRESS_LENGTH = 255;

    private HandshakeC2SPacket(PacketByteBuf buf) {
        this(buf.readVarInt(), buf.readString(255), buf.readUnsignedShort(), ConnectionIntent.byId(buf.readVarInt()));
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.protocolVersion);
        buf.writeString(this.address);
        buf.writeShort(this.port);
        buf.writeVarInt(this.intendedState.getId());
    }

    @Override
    public PacketType<HandshakeC2SPacket> getPacketType() {
        return HandshakePackets.INTENTION;
    }

    @Override
    public void apply(ServerHandshakePacketListener arg) {
        arg.onHandshake(this);
    }

    @Override
    public boolean transitionsNetworkState() {
        return true;
    }
}

