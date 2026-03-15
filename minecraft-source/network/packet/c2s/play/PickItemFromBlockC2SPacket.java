/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPickItemFromBlock(Lnet/minecraft/network/packet/c2s/play/PickItemFromBlockC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PickItemFromBlockC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public record PickItemFromBlockC2SPacket(BlockPos pos, boolean includeData) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, PickItemFromBlockC2SPacket> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, PickItemFromBlockC2SPacket::pos, PacketCodecs.BOOLEAN, PickItemFromBlockC2SPacket::includeData, PickItemFromBlockC2SPacket::new);

    @Override
    public PacketType<PickItemFromBlockC2SPacket> getPacketType() {
        return PlayPackets.PICK_ITEM_FROM_BLOCK;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPickItemFromBlock(this);
    }
}

