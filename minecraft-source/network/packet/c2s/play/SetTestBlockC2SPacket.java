/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onSetTestBlock(Lnet/minecraft/network/packet/c2s/play/SetTestBlockC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/SetTestBlockC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.block.enums.TestBlockMode;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public record SetTestBlockC2SPacket(BlockPos position, TestBlockMode mode, String message) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, SetTestBlockC2SPacket> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, SetTestBlockC2SPacket::position, TestBlockMode.PACKET_CODEC, SetTestBlockC2SPacket::mode, PacketCodecs.STRING, SetTestBlockC2SPacket::message, SetTestBlockC2SPacket::new);

    @Override
    public PacketType<SetTestBlockC2SPacket> getPacketType() {
        return PlayPackets.SET_TEST_BLOCK;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onSetTestBlock(this);
    }
}

