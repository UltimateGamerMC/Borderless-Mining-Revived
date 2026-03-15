/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerRotation(Lnet/minecraft/network/packet/s2c/play/PlayerRotationS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerRotationS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record PlayerRotationS2CPacket(float yaw, boolean relativeYaw, float pitch, boolean relativePitch) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerRotationS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, PlayerRotationS2CPacket::yaw, PacketCodecs.BOOLEAN, PlayerRotationS2CPacket::relativeYaw, PacketCodecs.FLOAT, PlayerRotationS2CPacket::pitch, PacketCodecs.BOOLEAN, PlayerRotationS2CPacket::relativePitch, PlayerRotationS2CPacket::new);

    @Override
    public PacketType<PlayerRotationS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_ROTATION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerRotation(this);
    }
}

