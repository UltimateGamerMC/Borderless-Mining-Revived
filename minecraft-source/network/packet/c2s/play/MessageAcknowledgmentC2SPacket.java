/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onMessageAcknowledgment(Lnet/minecraft/network/packet/c2s/play/MessageAcknowledgmentC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/MessageAcknowledgmentC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record MessageAcknowledgmentC2SPacket(int offset) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, MessageAcknowledgmentC2SPacket> CODEC = Packet.createCodec(MessageAcknowledgmentC2SPacket::write, MessageAcknowledgmentC2SPacket::new);

    private MessageAcknowledgmentC2SPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.offset);
    }

    @Override
    public PacketType<MessageAcknowledgmentC2SPacket> getPacketType() {
        return PlayPackets.CHAT_ACK;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onMessageAcknowledgment(this);
    }
}

