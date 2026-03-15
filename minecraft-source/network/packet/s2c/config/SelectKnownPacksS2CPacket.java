/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientConfigurationPacketListener;onSelectKnownPacks(Lnet/minecraft/network/packet/s2c/config/SelectKnownPacksS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/config/SelectKnownPacksS2CPacket;apply(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.s2c.config;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.registry.VersionedIdentifier;

public record SelectKnownPacksS2CPacket(List<VersionedIdentifier> knownPacks) implements Packet<ClientConfigurationPacketListener>
{
    public static final PacketCodec<ByteBuf, SelectKnownPacksS2CPacket> CODEC = PacketCodec.tuple(VersionedIdentifier.PACKET_CODEC.collect(PacketCodecs.toList()), SelectKnownPacksS2CPacket::knownPacks, SelectKnownPacksS2CPacket::new);

    @Override
    public PacketType<SelectKnownPacksS2CPacket> getPacketType() {
        return ConfigPackets.SELECT_KNOWN_PACKS_S2C;
    }

    @Override
    public void apply(ClientConfigurationPacketListener arg) {
        arg.onSelectKnownPacks(this);
    }
}

