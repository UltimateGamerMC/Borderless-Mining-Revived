/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onGameMessage(Lnet/minecraft/network/packet/s2c/play/GameMessageS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/GameMessageS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record GameMessageS2CPacket(Text content, boolean overlay) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, GameMessageS2CPacket> CODEC = PacketCodec.tuple(TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, GameMessageS2CPacket::content, PacketCodecs.BOOLEAN, GameMessageS2CPacket::overlay, GameMessageS2CPacket::new);

    @Override
    public PacketType<GameMessageS2CPacket> getPacketType() {
        return PlayPackets.SYSTEM_CHAT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onGameMessage(this);
    }

    @Override
    public boolean isWritingErrorSkippable() {
        return true;
    }
}

