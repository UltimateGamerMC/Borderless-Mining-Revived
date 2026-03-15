/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerSpawnPosition(Lnet/minecraft/network/packet/s2c/play/PlayerSpawnPositionS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerSpawnPositionS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.WorldProperties;

public record PlayerSpawnPositionS2CPacket(WorldProperties.SpawnPoint respawnData) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerSpawnPositionS2CPacket> CODEC = PacketCodec.tuple(WorldProperties.SpawnPoint.PACKET_CODEC, PlayerSpawnPositionS2CPacket::respawnData, PlayerSpawnPositionS2CPacket::new);

    @Override
    public PacketType<PlayerSpawnPositionS2CPacket> getPacketType() {
        return PlayPackets.SET_DEFAULT_SPAWN_POSITION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerSpawnPosition(this);
    }
}

