/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onPing(Lnet/minecraft/network/packet/s2c/common/CommonPingS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/CommonPingS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class CommonPingS2CPacket
implements Packet<ClientCommonPacketListener> {
    public static final PacketCodec<PacketByteBuf, CommonPingS2CPacket> CODEC = Packet.createCodec(CommonPingS2CPacket::write, CommonPingS2CPacket::new);
    private final int parameter;

    public CommonPingS2CPacket(int parameter) {
        this.parameter = parameter;
    }

    private CommonPingS2CPacket(PacketByteBuf buf) {
        this.parameter = buf.readInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.parameter);
    }

    @Override
    public PacketType<CommonPingS2CPacket> getPacketType() {
        return CommonPackets.PING;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onPing(this);
    }

    public int getParameter() {
        return this.parameter;
    }
}

