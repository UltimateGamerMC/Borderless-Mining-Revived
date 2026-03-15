/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readInstant()Ljava/time/Instant;
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeInstant(Ljava/time/Instant;)V
 *   Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import java.time.Instant;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import org.jspecify.annotations.Nullable;

public record ChatMessageC2SPacket(String chatMessage, Instant timestamp, long salt, @Nullable MessageSignatureData signature, LastSeenMessageList.Acknowledgment acknowledgment) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ChatMessageC2SPacket> CODEC = Packet.createCodec(ChatMessageC2SPacket::write, ChatMessageC2SPacket::new);

    private ChatMessageC2SPacket(PacketByteBuf buf) {
        this(buf.readString(256), buf.readInstant(), buf.readLong(), buf.readNullable(MessageSignatureData::fromBuf), new LastSeenMessageList.Acknowledgment(buf));
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.chatMessage, 256);
        buf.writeInstant(this.timestamp);
        buf.writeLong(this.salt);
        buf.writeNullable(this.signature, MessageSignatureData::write);
        this.acknowledgment.write(buf);
    }

    @Override
    public PacketType<ChatMessageC2SPacket> getPacketType() {
        return PlayPackets.CHAT;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onChatMessage(this);
    }
}

