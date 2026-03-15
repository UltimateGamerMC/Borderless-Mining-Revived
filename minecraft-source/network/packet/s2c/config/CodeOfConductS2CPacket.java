/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientConfigurationPacketListener;onCodeOfConduct(Lnet/minecraft/network/packet/s2c/config/CodeOfConductS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/config/CodeOfConductS2CPacket;apply(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.s2c.config;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record CodeOfConductS2CPacket(String codeOfConduct) implements Packet<ClientConfigurationPacketListener>
{
    public static final PacketCodec<ByteBuf, CodeOfConductS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, CodeOfConductS2CPacket::codeOfConduct, CodeOfConductS2CPacket::new);

    @Override
    public PacketType<CodeOfConductS2CPacket> getPacketType() {
        return ConfigPackets.CODE_OF_CONDUCT;
    }

    @Override
    public void apply(ClientConfigurationPacketListener arg) {
        arg.onCodeOfConduct(this);
    }
}

