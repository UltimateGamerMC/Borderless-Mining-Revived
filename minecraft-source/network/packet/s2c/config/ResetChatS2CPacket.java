/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientConfigurationPacketListener;onResetChat(Lnet/minecraft/network/packet/s2c/config/ResetChatS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/config/ResetChatS2CPacket;apply(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V
 */
package net.minecraft.network.packet.s2c.config;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.ConfigPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class ResetChatS2CPacket
implements Packet<ClientConfigurationPacketListener> {
    public static final ResetChatS2CPacket INSTANCE = new ResetChatS2CPacket();
    public static final PacketCodec<ByteBuf, ResetChatS2CPacket> CODEC = PacketCodec.unit(INSTANCE);

    private ResetChatS2CPacket() {
    }

    @Override
    public PacketType<ResetChatS2CPacket> getPacketType() {
        return ConfigPackets.RESET_CHAT;
    }

    @Override
    public void apply(ClientConfigurationPacketListener arg) {
        arg.onResetChat(this);
    }
}

