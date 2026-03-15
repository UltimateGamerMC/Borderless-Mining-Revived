/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeSyncId(I)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onCloseScreen(Lnet/minecraft/network/packet/s2c/play/CloseScreenS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/CloseScreenS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class CloseScreenS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, CloseScreenS2CPacket> CODEC = Packet.createCodec(CloseScreenS2CPacket::write, CloseScreenS2CPacket::new);
    private final int syncId;

    public CloseScreenS2CPacket(int syncId) {
        this.syncId = syncId;
    }

    private CloseScreenS2CPacket(PacketByteBuf buf) {
        this.syncId = buf.readSyncId();
    }

    private void write(PacketByteBuf buf) {
        buf.writeSyncId(this.syncId);
    }

    @Override
    public PacketType<CloseScreenS2CPacket> getPacketType() {
        return PlayPackets.CONTAINER_CLOSE_S2C;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onCloseScreen(this);
    }

    public int getSyncId() {
        return this.syncId;
    }
}

