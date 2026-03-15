/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeSyncId(I)V
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onCloseHandledScreen(Lnet/minecraft/network/packet/c2s/play/CloseHandledScreenC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/CloseHandledScreenC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class CloseHandledScreenC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, CloseHandledScreenC2SPacket> CODEC = Packet.createCodec(CloseHandledScreenC2SPacket::write, CloseHandledScreenC2SPacket::new);
    private final int syncId;

    public CloseHandledScreenC2SPacket(int syncId) {
        this.syncId = syncId;
    }

    private CloseHandledScreenC2SPacket(PacketByteBuf buf) {
        this.syncId = buf.readSyncId();
    }

    private void write(PacketByteBuf buf) {
        buf.writeSyncId(this.syncId);
    }

    @Override
    public PacketType<CloseHandledScreenC2SPacket> getPacketType() {
        return PlayPackets.CONTAINER_CLOSE_C2S;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onCloseHandledScreen(this);
    }

    public int getSyncId() {
        return this.syncId;
    }
}

