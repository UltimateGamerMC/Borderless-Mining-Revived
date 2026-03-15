/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEnterReconfiguration(Lnet/minecraft/network/packet/s2c/play/EnterReconfigurationS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EnterReconfigurationS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class EnterReconfigurationS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final EnterReconfigurationS2CPacket INSTANCE = new EnterReconfigurationS2CPacket();
    public static final PacketCodec<ByteBuf, EnterReconfigurationS2CPacket> CODEC = PacketCodec.unit(INSTANCE);

    private EnterReconfigurationS2CPacket() {
    }

    @Override
    public PacketType<EnterReconfigurationS2CPacket> getPacketType() {
        return PlayPackets.START_CONFIGURATION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEnterReconfiguration(this);
    }

    @Override
    public boolean transitionsNetworkState() {
        return true;
    }
}

