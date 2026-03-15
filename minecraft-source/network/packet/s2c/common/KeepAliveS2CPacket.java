/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onKeepAlive(Lnet/minecraft/network/packet/s2c/common/KeepAliveS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/KeepAliveS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class KeepAliveS2CPacket
implements Packet<ClientCommonPacketListener> {
    public static final PacketCodec<PacketByteBuf, KeepAliveS2CPacket> CODEC = Packet.createCodec(KeepAliveS2CPacket::write, KeepAliveS2CPacket::new);
    private final long id;

    public KeepAliveS2CPacket(long id) {
        this.id = id;
    }

    private KeepAliveS2CPacket(PacketByteBuf buf) {
        this.id = buf.readLong();
    }

    private void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    @Override
    public PacketType<KeepAliveS2CPacket> getPacketType() {
        return CommonPackets.KEEP_ALIVE_S2C;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onKeepAlive(this);
    }

    public long getId() {
        return this.id;
    }
}

