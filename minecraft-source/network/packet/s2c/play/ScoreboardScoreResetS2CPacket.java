/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onScoreboardScoreReset(Lnet/minecraft/network/packet/s2c/play/ScoreboardScoreResetS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ScoreboardScoreResetS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import org.jspecify.annotations.Nullable;

public record ScoreboardScoreResetS2CPacket(String scoreHolderName, @Nullable String objectiveName) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ScoreboardScoreResetS2CPacket> CODEC = Packet.createCodec(ScoreboardScoreResetS2CPacket::write, ScoreboardScoreResetS2CPacket::new);

    private ScoreboardScoreResetS2CPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readNullable(PacketByteBuf::readString));
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.scoreHolderName);
        buf.writeNullable(this.objectiveName, PacketByteBuf::writeString);
    }

    @Override
    public PacketType<ScoreboardScoreResetS2CPacket> getPacketType() {
        return PlayPackets.RESET_SCORE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onScoreboardScoreReset(this);
    }
}

