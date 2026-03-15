/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onGameTestHighlightPos(Lnet/minecraft/network/packet/s2c/play/GameTestHighlightPosS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/GameTestHighlightPosS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public record GameTestHighlightPosS2CPacket(BlockPos absolutePos, BlockPos relativePos) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, GameTestHighlightPosS2CPacket> PACKET_CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, GameTestHighlightPosS2CPacket::absolutePos, BlockPos.PACKET_CODEC, GameTestHighlightPosS2CPacket::relativePos, GameTestHighlightPosS2CPacket::new);

    @Override
    public PacketType<GameTestHighlightPosS2CPacket> getPacketType() {
        return PlayPackets.GAME_TEST_HIGHLIGHT_POS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onGameTestHighlightPos(this);
    }
}

