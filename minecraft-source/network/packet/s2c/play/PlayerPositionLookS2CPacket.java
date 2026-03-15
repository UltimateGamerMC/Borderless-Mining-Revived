/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerPositionLook(Lnet/minecraft/network/packet/s2c/play/PlayerPositionLookS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerPositionLookS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Set;
import net.minecraft.entity.EntityPosition;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public record PlayerPositionLookS2CPacket(int teleportId, EntityPosition change, Set<PositionFlag> relatives) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerPositionLookS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PlayerPositionLookS2CPacket::teleportId, EntityPosition.PACKET_CODEC, PlayerPositionLookS2CPacket::change, PositionFlag.PACKET_CODEC, PlayerPositionLookS2CPacket::relatives, PlayerPositionLookS2CPacket::new);

    public static PlayerPositionLookS2CPacket of(int teleportId, EntityPosition pos, Set<PositionFlag> flags) {
        return new PlayerPositionLookS2CPacket(teleportId, pos, flags);
    }

    @Override
    public PacketType<PlayerPositionLookS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_POSITION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerPositionLook(this);
    }
}

