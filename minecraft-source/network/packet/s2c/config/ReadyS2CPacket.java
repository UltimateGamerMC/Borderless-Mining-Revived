/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientConfigurationPacketListener;onReady(Lnet/minecraft/network/packet/s2c/config/ReadyS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/config/ReadyS2CPacket;apply(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.s2c.config;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class ReadyS2CPacket
implements Packet<ClientConfigurationPacketListener> {
    public static final ReadyS2CPacket INSTANCE = new ReadyS2CPacket();
    public static final PacketCodec<ByteBuf, ReadyS2CPacket> CODEC = PacketCodec.unit(INSTANCE);

    private ReadyS2CPacket() {
    }

    @Override
    public PacketType<ReadyS2CPacket> getPacketType() {
        return ConfigPackets.FINISH_CONFIGURATION_S2C;
    }

    @Override
    public void apply(ClientConfigurationPacketListener arg) {
        arg.onReady(this);
    }

    @Override
    public boolean transitionsNetworkState() {
        return true;
    }
}

