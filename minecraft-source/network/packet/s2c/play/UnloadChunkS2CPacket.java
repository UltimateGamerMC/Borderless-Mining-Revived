/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readChunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/network/PacketByteBuf;writeChunkPos(Lnet/minecraft/util/math/ChunkPos;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onUnloadChunk(Lnet/minecraft/network/packet/s2c/play/UnloadChunkS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/UnloadChunkS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.ChunkPos;

public record UnloadChunkS2CPacket(ChunkPos pos) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, UnloadChunkS2CPacket> CODEC = Packet.createCodec(UnloadChunkS2CPacket::write, UnloadChunkS2CPacket::new);

    private UnloadChunkS2CPacket(PacketByteBuf buf) {
        this(buf.readChunkPos());
    }

    private void write(PacketByteBuf buf) {
        buf.writeChunkPos(this.pos);
    }

    @Override
    public PacketType<UnloadChunkS2CPacket> getPacketType() {
        return PlayPackets.FORGET_LEVEL_CHUNK;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onUnloadChunk(this);
    }
}

