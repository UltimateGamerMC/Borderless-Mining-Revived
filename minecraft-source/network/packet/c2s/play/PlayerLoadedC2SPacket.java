/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPlayerLoaded(Lnet/minecraft/network/packet/c2s/play/PlayerLoadedC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PlayerLoadedC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record PlayerLoadedC2SPacket() implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, PlayerLoadedC2SPacket> CODEC = PacketCodec.unit(new PlayerLoadedC2SPacket());

    @Override
    public PacketType<PlayerLoadedC2SPacket> getPacketType() {
        return PlayPackets.PLAYER_LOADED;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPlayerLoaded(this);
    }
}

