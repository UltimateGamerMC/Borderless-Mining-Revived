/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPickItemFromEntity(Lnet/minecraft/network/packet/c2s/play/PickItemFromEntityC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PickItemFromEntityC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record PickItemFromEntityC2SPacket(int id, boolean includeData) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, PickItemFromEntityC2SPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PickItemFromEntityC2SPacket::id, PacketCodecs.BOOLEAN, PickItemFromEntityC2SPacket::includeData, PickItemFromEntityC2SPacket::new);

    @Override
    public PacketType<PickItemFromEntityC2SPacket> getPacketType() {
        return PlayPackets.PICK_ITEM_FROM_ENTITY;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPickItemFromEntity(this);
    }
}

