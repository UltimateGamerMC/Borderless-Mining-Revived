/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldTimeUpdate(Lnet/minecraft/network/packet/s2c/play/WorldTimeUpdateS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldTimeUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record WorldTimeUpdateS2CPacket(long time, long timeOfDay, boolean tickDayTime) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, WorldTimeUpdateS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.LONG, WorldTimeUpdateS2CPacket::time, PacketCodecs.LONG, WorldTimeUpdateS2CPacket::timeOfDay, PacketCodecs.BOOLEAN, WorldTimeUpdateS2CPacket::tickDayTime, WorldTimeUpdateS2CPacket::new);

    @Override
    public PacketType<WorldTimeUpdateS2CPacket> getPacketType() {
        return PlayPackets.SET_TIME;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldTimeUpdate(this);
    }
}

