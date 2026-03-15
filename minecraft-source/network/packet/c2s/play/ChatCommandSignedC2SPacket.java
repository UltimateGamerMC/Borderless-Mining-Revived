/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readInstant()Ljava/time/Instant;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeInstant(Ljava/time/Instant;)V
 *   Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/message/ArgumentSignatureDataMap;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onChatCommandSigned(Lnet/minecraft/network/packet/c2s/play/ChatCommandSignedC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/ChatCommandSignedC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import java.time.Instant;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record ChatCommandSignedC2SPacket(String command, Instant timestamp, long salt, ArgumentSignatureDataMap argumentSignatures, LastSeenMessageList.Acknowledgment lastSeenMessages) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ChatCommandSignedC2SPacket> CODEC = Packet.createCodec(ChatCommandSignedC2SPacket::write, ChatCommandSignedC2SPacket::new);

    private ChatCommandSignedC2SPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readInstant(), buf.readLong(), new ArgumentSignatureDataMap(buf), new LastSeenMessageList.Acknowledgment(buf));
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.command);
        buf.writeInstant(this.timestamp);
        buf.writeLong(this.salt);
        this.argumentSignatures.write(buf);
        this.lastSeenMessages.write(buf);
    }

    @Override
    public PacketType<ChatCommandSignedC2SPacket> getPacketType() {
        return PlayPackets.CHAT_COMMAND_SIGNED;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onChatCommandSigned(this);
    }
}

