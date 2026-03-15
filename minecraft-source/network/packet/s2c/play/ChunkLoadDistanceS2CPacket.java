/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onChunkLoadDistance(Lnet/minecraft/network/packet/s2c/play/ChunkLoadDistanceS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ChunkLoadDistanceS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class ChunkLoadDistanceS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, ChunkLoadDistanceS2CPacket> CODEC = Packet.createCodec(ChunkLoadDistanceS2CPacket::write, ChunkLoadDistanceS2CPacket::new);
    private final int distance;

    public ChunkLoadDistanceS2CPacket(int distance) {
        this.distance = distance;
    }

    private ChunkLoadDistanceS2CPacket(PacketByteBuf buf) {
        this.distance = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.distance);
    }

    @Override
    public PacketType<ChunkLoadDistanceS2CPacket> getPacketType() {
        return PlayPackets.SET_CHUNK_CACHE_RADIUS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onChunkLoadDistance(this);
    }

    public int getDistance() {
        return this.distance;
    }
}

