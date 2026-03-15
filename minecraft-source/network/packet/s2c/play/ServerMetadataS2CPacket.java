/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onServerMetadata(Lnet/minecraft/network/packet/s2c/play/ServerMetadataS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ServerMetadataS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record ServerMetadataS2CPacket(Text description, Optional<byte[]> favicon) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, ServerMetadataS2CPacket> CODEC = PacketCodec.tuple(TextCodecs.PACKET_CODEC, ServerMetadataS2CPacket::description, PacketCodecs.BYTE_ARRAY.collect(PacketCodecs::optional), ServerMetadataS2CPacket::favicon, ServerMetadataS2CPacket::new);

    @Override
    public PacketType<ServerMetadataS2CPacket> getPacketType() {
        return PlayPackets.SERVER_DATA;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onServerMetadata(this);
    }
}

