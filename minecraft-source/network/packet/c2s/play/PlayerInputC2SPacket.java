/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPlayerInput(Lnet/minecraft/network/packet/c2s/play/PlayerInputC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PlayerInputC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.PlayerInput;

public record PlayerInputC2SPacket(PlayerInput input) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerInputC2SPacket> CODEC = PacketCodec.tuple(PlayerInput.PACKET_CODEC, PlayerInputC2SPacket::input, PlayerInputC2SPacket::new);

    @Override
    public PacketType<PlayerInputC2SPacket> getPacketType() {
        return PlayPackets.PLAYER_INPUT;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPlayerInput(this);
    }
}

