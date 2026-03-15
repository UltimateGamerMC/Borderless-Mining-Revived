/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerCommonPacketListener;onPong(Lnet/minecraft/network/packet/c2s/common/CommonPongC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/common/CommonPongC2SPacket;apply(Lnet/minecraft/network/listener/ServerCommonPacketListener;)V
 */
package net.minecraft.network.packet.c2s.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class CommonPongC2SPacket
implements Packet<ServerCommonPacketListener> {
    public static final PacketCodec<PacketByteBuf, CommonPongC2SPacket> CODEC = Packet.createCodec(CommonPongC2SPacket::write, CommonPongC2SPacket::new);
    private final int parameter;

    public CommonPongC2SPacket(int parameter) {
        this.parameter = parameter;
    }

    private CommonPongC2SPacket(PacketByteBuf buf) {
        this.parameter = buf.readInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.parameter);
    }

    @Override
    public PacketType<CommonPongC2SPacket> getPacketType() {
        return CommonPackets.PONG;
    }

    @Override
    public void apply(ServerCommonPacketListener arg) {
        arg.onPong(this);
    }

    public int getParameter() {
        return this.parameter;
    }
}

