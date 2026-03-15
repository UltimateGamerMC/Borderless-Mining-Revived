/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onResourcePackSend(Lnet/minecraft/network/packet/s2c/common/ResourcePackSendS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;string(I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function5;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/ResourcePackSendS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Uuids;

public record ResourcePackSendS2CPacket(UUID id, String url, String hash, boolean required, Optional<Text> prompt) implements Packet<ClientCommonPacketListener>
{
    public static final int MAX_HASH_LENGTH = 40;
    public static final PacketCodec<ByteBuf, ResourcePackSendS2CPacket> CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, ResourcePackSendS2CPacket::id, PacketCodecs.STRING, ResourcePackSendS2CPacket::url, PacketCodecs.string(40), ResourcePackSendS2CPacket::hash, PacketCodecs.BOOLEAN, ResourcePackSendS2CPacket::required, TextCodecs.PACKET_CODEC.collect(PacketCodecs::optional), ResourcePackSendS2CPacket::prompt, ResourcePackSendS2CPacket::new);

    public ResourcePackSendS2CPacket {
        if (hash.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
        }
    }

    @Override
    public PacketType<ResourcePackSendS2CPacket> getPacketType() {
        return CommonPackets.RESOURCE_PACK_PUSH;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onResourcePackSend(this);
    }
}

