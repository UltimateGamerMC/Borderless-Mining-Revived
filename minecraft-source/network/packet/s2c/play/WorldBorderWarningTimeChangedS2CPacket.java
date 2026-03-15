/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldBorderWarningTimeChanged(Lnet/minecraft/network/packet/s2c/play/WorldBorderWarningTimeChangedS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldBorderWarningTimeChangedS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderWarningTimeChangedS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, WorldBorderWarningTimeChangedS2CPacket> CODEC = Packet.createCodec(WorldBorderWarningTimeChangedS2CPacket::write, WorldBorderWarningTimeChangedS2CPacket::new);
    private final int warningTime;

    public WorldBorderWarningTimeChangedS2CPacket(WorldBorder worldBorder) {
        this.warningTime = worldBorder.getWarningTime();
    }

    private WorldBorderWarningTimeChangedS2CPacket(PacketByteBuf buf) {
        this.warningTime = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.warningTime);
    }

    @Override
    public PacketType<WorldBorderWarningTimeChangedS2CPacket> getPacketType() {
        return PlayPackets.SET_BORDER_WARNING_DELAY;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldBorderWarningTimeChanged(this);
    }

    public int getWarningTime() {
        return this.warningTime;
    }
}

