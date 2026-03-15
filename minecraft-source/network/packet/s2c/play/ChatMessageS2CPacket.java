/*
 * External method calls:
 *   Lnet/minecraft/network/RegistryByteBuf;readUuid()Ljava/util/UUID;
 *   Lnet/minecraft/network/RegistryByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lio/netty/buffer/ByteBuf;Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/message/FilterMask;readMask(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/message/FilterMask;
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeUuid(Ljava/util/UUID;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/message/MessageBody$Serialized;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Lio/netty/buffer/ByteBuf;Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/message/FilterMask;writeMask(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/message/FilterMask;)V
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onChatMessage(Lnet/minecraft/network/packet/s2c/play/ChatMessageS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ChatMessageS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.message.FilterMask;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jspecify.annotations.Nullable;

public record ChatMessageS2CPacket(int globalIndex, UUID sender, int index, @Nullable MessageSignatureData signature, MessageBody.Serialized body, @Nullable Text unsignedContent, FilterMask filterMask, MessageType.Parameters serializedParameters) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, ChatMessageS2CPacket> CODEC = Packet.createCodec(ChatMessageS2CPacket::write, ChatMessageS2CPacket::new);

    private ChatMessageS2CPacket(RegistryByteBuf buf) {
        this(buf.readVarInt(), buf.readUuid(), buf.readVarInt(), buf.readNullable(MessageSignatureData::fromBuf), new MessageBody.Serialized(buf), PacketByteBuf.readNullable(buf, TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC), FilterMask.readMask(buf), (MessageType.Parameters)MessageType.Parameters.CODEC.decode(buf));
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.globalIndex);
        buf.writeUuid(this.sender);
        buf.writeVarInt(this.index);
        buf.writeNullable(this.signature, MessageSignatureData::write);
        this.body.write(buf);
        PacketByteBuf.writeNullable(buf, this.unsignedContent, TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC);
        FilterMask.writeMask(buf, this.filterMask);
        MessageType.Parameters.CODEC.encode(buf, this.serializedParameters);
    }

    @Override
    public PacketType<ChatMessageS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_CHAT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onChatMessage(this);
    }

    @Override
    public boolean isWritingErrorSkippable() {
        return true;
    }
}

