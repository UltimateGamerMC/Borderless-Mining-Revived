/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerConfigurationPacketListener;onAcceptCodeOfConduct(Lnet/minecraft/network/packet/c2s/config/AcceptCodeOfConductC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/config/AcceptCodeOfConductC2SPacket;apply(Lnet/minecraft/network/listener/ServerConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.c2s.config;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record AcceptCodeOfConductC2SPacket() implements Packet<ServerConfigurationPacketListener>
{
    public static final AcceptCodeOfConductC2SPacket INSTANCE = new AcceptCodeOfConductC2SPacket();
    public static final PacketCodec<ByteBuf, AcceptCodeOfConductC2SPacket> CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public PacketType<AcceptCodeOfConductC2SPacket> getPacketType() {
        return ConfigPackets.ACCEPT_CODE_OF_CONDUCT;
    }

    @Override
    public void apply(ServerConfigurationPacketListener arg) {
        arg.onAcceptCodeOfConduct(this);
    }
}

