/*
 * External method calls:
 *   Lnet/minecraft/network/RegistryByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/RegistryByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onScoreboardObjectiveUpdate(Lnet/minecraft/network/packet/s2c/play/ScoreboardObjectiveUpdateS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ScoreboardObjectiveUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Optional;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.NumberFormatTypes;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class ScoreboardObjectiveUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, ScoreboardObjectiveUpdateS2CPacket> CODEC = Packet.createCodec(ScoreboardObjectiveUpdateS2CPacket::write, ScoreboardObjectiveUpdateS2CPacket::new);
    public static final int ADD_MODE = 0;
    public static final int REMOVE_MODE = 1;
    public static final int UPDATE_MODE = 2;
    private final String name;
    private final Text displayName;
    private final ScoreboardCriterion.RenderType type;
    private final Optional<NumberFormat> numberFormat;
    private final int mode;

    public ScoreboardObjectiveUpdateS2CPacket(ScoreboardObjective objective, int mode) {
        this.name = objective.getName();
        this.displayName = objective.getDisplayName();
        this.type = objective.getRenderType();
        this.numberFormat = Optional.ofNullable(objective.getNumberFormat());
        this.mode = mode;
    }

    private ScoreboardObjectiveUpdateS2CPacket(RegistryByteBuf buf) {
        this.name = buf.readString();
        this.mode = buf.readByte();
        if (this.mode == 0 || this.mode == 2) {
            this.displayName = (Text)TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC.decode(buf);
            this.type = buf.readEnumConstant(ScoreboardCriterion.RenderType.class);
            this.numberFormat = (Optional)NumberFormatTypes.OPTIONAL_PACKET_CODEC.decode(buf);
        } else {
            this.displayName = ScreenTexts.EMPTY;
            this.type = ScoreboardCriterion.RenderType.INTEGER;
            this.numberFormat = Optional.empty();
        }
    }

    private void write(RegistryByteBuf buf) {
        buf.writeString(this.name);
        buf.writeByte(this.mode);
        if (this.mode == 0 || this.mode == 2) {
            TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC.encode(buf, this.displayName);
            buf.writeEnumConstant(this.type);
            NumberFormatTypes.OPTIONAL_PACKET_CODEC.encode(buf, this.numberFormat);
        }
    }

    @Override
    public PacketType<ScoreboardObjectiveUpdateS2CPacket> getPacketType() {
        return PlayPackets.SET_OBJECTIVE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onScoreboardObjectiveUpdate(this);
    }

    public String getName() {
        return this.name;
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public int getMode() {
        return this.mode;
    }

    public ScoreboardCriterion.RenderType getType() {
        return this.type;
    }

    public Optional<NumberFormat> getNumberFormat() {
        return this.numberFormat;
    }
}

