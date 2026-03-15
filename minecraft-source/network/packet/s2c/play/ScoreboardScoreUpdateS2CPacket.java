/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onScoreboardScoreUpdate(Lnet/minecraft/network/packet/s2c/play/ScoreboardScoreUpdateS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function5;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ScoreboardScoreUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Optional;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.NumberFormatTypes;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record ScoreboardScoreUpdateS2CPacket(String scoreHolderName, String objectiveName, int score, Optional<Text> display, Optional<NumberFormat> numberFormat) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, ScoreboardScoreUpdateS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ScoreboardScoreUpdateS2CPacket::scoreHolderName, PacketCodecs.STRING, ScoreboardScoreUpdateS2CPacket::objectiveName, PacketCodecs.VAR_INT, ScoreboardScoreUpdateS2CPacket::score, TextCodecs.OPTIONAL_UNLIMITED_REGISTRY_PACKET_CODEC, ScoreboardScoreUpdateS2CPacket::display, NumberFormatTypes.OPTIONAL_PACKET_CODEC, ScoreboardScoreUpdateS2CPacket::numberFormat, ScoreboardScoreUpdateS2CPacket::new);

    @Override
    public PacketType<ScoreboardScoreUpdateS2CPacket> getPacketType() {
        return PlayPackets.SET_SCORE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onScoreboardScoreUpdate(this);
    }
}

