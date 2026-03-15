/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/PacketByteBuf;readList(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/List;
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onChatSuggestions(Lnet/minecraft/network/packet/s2c/play/ChatSuggestionsS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ChatSuggestionsS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.List;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record ChatSuggestionsS2CPacket(Action action, List<String> entries) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ChatSuggestionsS2CPacket> CODEC = Packet.createCodec(ChatSuggestionsS2CPacket::write, ChatSuggestionsS2CPacket::new);

    private ChatSuggestionsS2CPacket(PacketByteBuf buf) {
        this(buf.readEnumConstant(Action.class), buf.readList(PacketByteBuf::readString));
    }

    private void write(PacketByteBuf buf) {
        buf.writeEnumConstant(this.action);
        buf.writeCollection(this.entries, PacketByteBuf::writeString);
    }

    @Override
    public PacketType<ChatSuggestionsS2CPacket> getPacketType() {
        return PlayPackets.CUSTOM_CHAT_COMPLETIONS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onChatSuggestions(this);
    }

    public static enum Action {
        ADD,
        REMOVE,
        SET;

    }
}

