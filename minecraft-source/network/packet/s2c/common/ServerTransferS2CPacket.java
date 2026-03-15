/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onServerTransfer(Lnet/minecraft/network/packet/s2c/common/ServerTransferS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/ServerTransferS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record ServerTransferS2CPacket(String host, int port) implements Packet<ClientCommonPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ServerTransferS2CPacket> CODEC = Packet.createCodec(ServerTransferS2CPacket::write, ServerTransferS2CPacket::new);

    private ServerTransferS2CPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.host);
        buf.writeVarInt(this.port);
    }

    @Override
    public PacketType<ServerTransferS2CPacket> getPacketType() {
        return CommonPackets.TRANSFER;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onServerTransfer(this);
    }
}

