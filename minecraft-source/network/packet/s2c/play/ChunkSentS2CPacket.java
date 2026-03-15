/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onChunkSent(Lnet/minecraft/network/packet/s2c/play/ChunkSentS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ChunkSentS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record ChunkSentS2CPacket(int batchSize) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ChunkSentS2CPacket> CODEC = Packet.createCodec(ChunkSentS2CPacket::write, ChunkSentS2CPacket::new);

    private ChunkSentS2CPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.batchSize);
    }

    @Override
    public PacketType<ChunkSentS2CPacket> getPacketType() {
        return PlayPackets.CHUNK_BATCH_FINISHED;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onChunkSent(this);
    }
}

