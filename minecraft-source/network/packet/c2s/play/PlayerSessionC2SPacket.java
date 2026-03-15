/*
 * External method calls:
 *   Lnet/minecraft/network/encryption/PublicPlayerSession$Serialized;fromBuf(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/encryption/PublicPlayerSession$Serialized;
 *   Lnet/minecraft/network/encryption/PublicPlayerSession$Serialized;write(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/encryption/PublicPlayerSession$Serialized;)V
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPlayerSession(Lnet/minecraft/network/packet/c2s/play/PlayerSessionC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PlayerSessionC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.encryption.PublicPlayerSession;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record PlayerSessionC2SPacket(PublicPlayerSession.Serialized chatSession) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerSessionC2SPacket> CODEC = Packet.createCodec(PlayerSessionC2SPacket::write, PlayerSessionC2SPacket::new);

    private PlayerSessionC2SPacket(PacketByteBuf buf) {
        this(PublicPlayerSession.Serialized.fromBuf(buf));
    }

    private void write(PacketByteBuf buf) {
        PublicPlayerSession.Serialized.write(buf, this.chatSession);
    }

    @Override
    public PacketType<PlayerSessionC2SPacket> getPacketType() {
        return PlayPackets.CHAT_SESSION_UPDATE;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPlayerSession(this);
    }
}

