/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onDisconnect(Lnet/minecraft/network/packet/s2c/common/DisconnectS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/DisconnectS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record DisconnectS2CPacket(Text reason) implements Packet<ClientCommonPacketListener>
{
    public static final PacketCodec<ByteBuf, DisconnectS2CPacket> CODEC = TextCodecs.PACKET_CODEC.xmap(DisconnectS2CPacket::new, DisconnectS2CPacket::reason);

    @Override
    public PacketType<DisconnectS2CPacket> getPacketType() {
        return CommonPackets.DISCONNECT;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onDisconnect(this);
    }
}

