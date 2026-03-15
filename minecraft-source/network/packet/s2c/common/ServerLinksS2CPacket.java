/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onServerLinks(Lnet/minecraft/network/packet/s2c/common/ServerLinksS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/ServerLinksS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.server.ServerLinks;

public record ServerLinksS2CPacket(List<ServerLinks.StringifiedEntry> links) implements Packet<ClientCommonPacketListener>
{
    public static final PacketCodec<ByteBuf, ServerLinksS2CPacket> CODEC = PacketCodec.tuple(ServerLinks.LIST_CODEC, ServerLinksS2CPacket::links, ServerLinksS2CPacket::new);

    @Override
    public PacketType<ServerLinksS2CPacket> getPacketType() {
        return CommonPackets.SERVER_LINKS;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onServerLinks(this);
    }
}

