/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onStartChunkSend(Lnet/minecraft/network/packet/s2c/play/StartChunkSendS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/StartChunkSendS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class StartChunkSendS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final StartChunkSendS2CPacket INSTANCE = new StartChunkSendS2CPacket();
    public static final PacketCodec<ByteBuf, StartChunkSendS2CPacket> CODEC = PacketCodec.unit(INSTANCE);

    private StartChunkSendS2CPacket() {
    }

    @Override
    public PacketType<StartChunkSendS2CPacket> getPacketType() {
        return PlayPackets.CHUNK_BATCH_START;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onStartChunkSend(this);
    }
}

