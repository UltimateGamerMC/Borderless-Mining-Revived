/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerLoginPacketListener;onEnterConfiguration(Lnet/minecraft/network/packet/c2s/login/EnterConfigurationC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/login/EnterConfigurationC2SPacket;apply(Lnet/minecraft/network/listener/ServerLoginPacketListener;)V
 */
package net.minecraft.network.packet.c2s.login;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class EnterConfigurationC2SPacket
implements Packet<ServerLoginPacketListener> {
    public static final EnterConfigurationC2SPacket INSTANCE = new EnterConfigurationC2SPacket();
    public static final PacketCodec<ByteBuf, EnterConfigurationC2SPacket> CODEC = PacketCodec.unit(INSTANCE);

    private EnterConfigurationC2SPacket() {
    }

    @Override
    public PacketType<EnterConfigurationC2SPacket> getPacketType() {
        return LoginPackets.LOGIN_ACKNOWLEDGED;
    }

    @Override
    public void apply(ServerLoginPacketListener arg) {
        arg.onEnterConfiguration(this);
    }

    @Override
    public boolean transitionsNetworkState() {
        return true;
    }
}

