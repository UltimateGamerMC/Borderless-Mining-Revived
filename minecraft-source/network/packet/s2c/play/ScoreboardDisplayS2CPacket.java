/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;decode(Ljava/util/function/IntFunction;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;encode(Ljava/util/function/ToIntFunction;Ljava/lang/Object;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onScoreboardDisplay(Lnet/minecraft/network/packet/s2c/play/ScoreboardDisplayS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ScoreboardDisplayS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Objects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jspecify.annotations.Nullable;

public class ScoreboardDisplayS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, ScoreboardDisplayS2CPacket> CODEC = Packet.createCodec(ScoreboardDisplayS2CPacket::write, ScoreboardDisplayS2CPacket::new);
    private final ScoreboardDisplaySlot slot;
    private final String name;

    public ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot slot, @Nullable ScoreboardObjective objective) {
        this.slot = slot;
        this.name = objective == null ? "" : objective.getName();
    }

    private ScoreboardDisplayS2CPacket(PacketByteBuf buf) {
        this.slot = buf.decode(ScoreboardDisplaySlot.FROM_ID);
        this.name = buf.readString();
    }

    private void write(PacketByteBuf buf) {
        buf.encode(ScoreboardDisplaySlot::getId, this.slot);
        buf.writeString(this.name);
    }

    @Override
    public PacketType<ScoreboardDisplayS2CPacket> getPacketType() {
        return PlayPackets.SET_DISPLAY_OBJECTIVE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onScoreboardDisplay(this);
    }

    public ScoreboardDisplaySlot getSlot() {
        return this.slot;
    }

    public @Nullable String getName() {
        return Objects.equals(this.name, "") ? null : this.name;
    }
}

