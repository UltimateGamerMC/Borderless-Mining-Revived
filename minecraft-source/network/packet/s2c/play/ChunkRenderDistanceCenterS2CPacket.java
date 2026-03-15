/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onChunkRenderDistanceCenter(Lnet/minecraft/network/packet/s2c/play/ChunkRenderDistanceCenterS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ChunkRenderDistanceCenterS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class ChunkRenderDistanceCenterS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, ChunkRenderDistanceCenterS2CPacket> CODEC = Packet.createCodec(ChunkRenderDistanceCenterS2CPacket::write, ChunkRenderDistanceCenterS2CPacket::new);
    private final int chunkX;
    private final int chunkZ;

    public ChunkRenderDistanceCenterS2CPacket(int x, int z) {
        this.chunkX = x;
        this.chunkZ = z;
    }

    private ChunkRenderDistanceCenterS2CPacket(PacketByteBuf buf) {
        this.chunkX = buf.readVarInt();
        this.chunkZ = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.chunkX);
        buf.writeVarInt(this.chunkZ);
    }

    @Override
    public PacketType<ChunkRenderDistanceCenterS2CPacket> getPacketType() {
        return PlayPackets.SET_CHUNK_CACHE_CENTER;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onChunkRenderDistanceCenter(this);
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }
}

