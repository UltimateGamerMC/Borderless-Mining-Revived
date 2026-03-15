/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeFloat(F)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onAcknowledgeChunks(Lnet/minecraft/network/packet/c2s/play/AcknowledgeChunksC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/AcknowledgeChunksC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record AcknowledgeChunksC2SPacket(float desiredChunksPerTick) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, AcknowledgeChunksC2SPacket> CODEC = Packet.createCodec(AcknowledgeChunksC2SPacket::write, AcknowledgeChunksC2SPacket::new);

    private AcknowledgeChunksC2SPacket(PacketByteBuf buf) {
        this(buf.readFloat());
    }

    private void write(PacketByteBuf buf) {
        buf.writeFloat(this.desiredChunksPerTick);
    }

    @Override
    public PacketType<AcknowledgeChunksC2SPacket> getPacketType() {
        return PlayPackets.CHUNK_BATCH_RECEIVED;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onAcknowledgeChunks(this);
    }
}

