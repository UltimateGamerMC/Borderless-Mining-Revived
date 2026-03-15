/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onUpdateBeacon(Lnet/minecraft/network/packet/c2s/play/UpdateBeaconC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/UpdateBeaconC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import java.util.Optional;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.entry.RegistryEntry;

public record UpdateBeaconC2SPacket(Optional<RegistryEntry<StatusEffect>> primary, Optional<RegistryEntry<StatusEffect>> secondary) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, UpdateBeaconC2SPacket> CODEC = PacketCodec.tuple(StatusEffect.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), UpdateBeaconC2SPacket::primary, StatusEffect.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), UpdateBeaconC2SPacket::secondary, UpdateBeaconC2SPacket::new);

    @Override
    public PacketType<UpdateBeaconC2SPacket> getPacketType() {
        return PlayPackets.SET_BEACON;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onUpdateBeacon(this);
    }
}

