/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onUpdateDifficulty(Lnet/minecraft/network/packet/c2s/play/UpdateDifficultyC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/UpdateDifficultyC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.Difficulty;

public record UpdateDifficultyC2SPacket(Difficulty difficulty) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, UpdateDifficultyC2SPacket> CODEC = PacketCodec.tuple(Difficulty.PACKET_CODEC, UpdateDifficultyC2SPacket::difficulty, UpdateDifficultyC2SPacket::new);

    @Override
    public PacketType<UpdateDifficultyC2SPacket> getPacketType() {
        return PlayPackets.CHANGE_DIFFICULTY_C2S;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onUpdateDifficulty(this);
    }
}

