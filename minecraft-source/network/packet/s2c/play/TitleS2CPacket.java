/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onTitle(Lnet/minecraft/network/packet/s2c/play/TitleS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/TitleS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record TitleS2CPacket(Text text) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, TitleS2CPacket> CODEC = PacketCodec.tuple(TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, TitleS2CPacket::text, TitleS2CPacket::new);

    @Override
    public PacketType<TitleS2CPacket> getPacketType() {
        return PlayPackets.SET_TITLE_TEXT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onTitle(this);
    }
}

