/*
 * External method calls:
 *   Lnet/minecraft/network/message/MessageSignatureData$Indexed;fromBuf(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/message/MessageSignatureData$Indexed;
 *   Lnet/minecraft/network/message/MessageSignatureData$Indexed;write(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/message/MessageSignatureData$Indexed;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onRemoveMessage(Lnet/minecraft/network/packet/s2c/play/RemoveMessageS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/RemoveMessageS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record RemoveMessageS2CPacket(MessageSignatureData.Indexed messageSignature) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, RemoveMessageS2CPacket> CODEC = Packet.createCodec(RemoveMessageS2CPacket::write, RemoveMessageS2CPacket::new);

    private RemoveMessageS2CPacket(PacketByteBuf buf) {
        this(MessageSignatureData.Indexed.fromBuf(buf));
    }

    private void write(PacketByteBuf buf) {
        MessageSignatureData.Indexed.write(buf, this.messageSignature);
    }

    @Override
    public PacketType<RemoveMessageS2CPacket> getPacketType() {
        return PlayPackets.DELETE_CHAT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onRemoveMessage(this);
    }
}

