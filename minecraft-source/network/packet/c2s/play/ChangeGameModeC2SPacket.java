/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onChangeGameMode(Lnet/minecraft/network/packet/c2s/play/ChangeGameModeC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/ChangeGameModeC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.GameMode;

public record ChangeGameModeC2SPacket(GameMode mode) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, ChangeGameModeC2SPacket> CODEC = PacketCodec.tuple(GameMode.PACKET_CODEC, ChangeGameModeC2SPacket::mode, ChangeGameModeC2SPacket::new);

    @Override
    public PacketType<ChangeGameModeC2SPacket> getPacketType() {
        return PlayPackets.CHANGE_GAME_MODE;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onChangeGameMode(this);
    }
}

