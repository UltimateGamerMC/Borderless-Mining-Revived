/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onDeathMessage(Lnet/minecraft/network/packet/s2c/play/DeathMessageS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/DeathMessageS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record DeathMessageS2CPacket(int playerId, Text message) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, DeathMessageS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, DeathMessageS2CPacket::playerId, TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, DeathMessageS2CPacket::message, DeathMessageS2CPacket::new);

    @Override
    public PacketType<DeathMessageS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_COMBAT_KILL;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onDeathMessage(this);
    }

    @Override
    public boolean isWritingErrorSkippable() {
        return true;
    }
}

