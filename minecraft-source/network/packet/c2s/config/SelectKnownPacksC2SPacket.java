/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerConfigurationPacketListener;onSelectKnownPacks(Lnet/minecraft/network/packet/c2s/config/SelectKnownPacksC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;toList(I)Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/config/SelectKnownPacksC2SPacket;apply(Lnet/minecraft/network/listener/ServerConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.c2s.config;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.registry.VersionedIdentifier;

public record SelectKnownPacksC2SPacket(List<VersionedIdentifier> knownPacks) implements Packet<ServerConfigurationPacketListener>
{
    public static final PacketCodec<ByteBuf, SelectKnownPacksC2SPacket> CODEC = PacketCodec.tuple(VersionedIdentifier.PACKET_CODEC.collect(PacketCodecs.toList(64)), SelectKnownPacksC2SPacket::knownPacks, SelectKnownPacksC2SPacket::new);

    @Override
    public PacketType<SelectKnownPacksC2SPacket> getPacketType() {
        return ConfigPackets.SELECT_KNOWN_PACKS_C2S;
    }

    @Override
    public void apply(ServerConfigurationPacketListener arg) {
        arg.onSelectKnownPacks(this);
    }
}

