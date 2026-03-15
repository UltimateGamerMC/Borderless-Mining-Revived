/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerCommonPacketListener;onKeepAlive(Lnet/minecraft/network/packet/c2s/common/KeepAliveC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/common/KeepAliveC2SPacket;apply(Lnet/minecraft/network/listener/ServerCommonPacketListener;)V
 */
package net.minecraft.network.packet.c2s.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class KeepAliveC2SPacket
implements Packet<ServerCommonPacketListener> {
    public static final PacketCodec<PacketByteBuf, KeepAliveC2SPacket> CODEC = Packet.createCodec(KeepAliveC2SPacket::write, KeepAliveC2SPacket::new);
    private final long id;

    public KeepAliveC2SPacket(long id) {
        this.id = id;
    }

    private KeepAliveC2SPacket(PacketByteBuf buf) {
        this.id = buf.readLong();
    }

    private void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    @Override
    public PacketType<KeepAliveC2SPacket> getPacketType() {
        return CommonPackets.KEEP_ALIVE_C2S;
    }

    @Override
    public void apply(ServerCommonPacketListener arg) {
        arg.onKeepAlive(this);
    }

    public long getId() {
        return this.id;
    }
}

