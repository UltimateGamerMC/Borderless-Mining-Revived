/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onCooldownUpdate(Lnet/minecraft/network/packet/s2c/play/CooldownUpdateS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/CooldownUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.Identifier;

public record CooldownUpdateS2CPacket(Identifier cooldownGroup, int cooldown) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, CooldownUpdateS2CPacket> CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, CooldownUpdateS2CPacket::cooldownGroup, PacketCodecs.VAR_INT, CooldownUpdateS2CPacket::cooldown, CooldownUpdateS2CPacket::new);

    @Override
    public PacketType<CooldownUpdateS2CPacket> getPacketType() {
        return PlayPackets.COOLDOWN;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onCooldownUpdate(this);
    }
}

