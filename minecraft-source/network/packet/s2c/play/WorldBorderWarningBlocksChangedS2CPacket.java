/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldBorderWarningBlocksChanged(Lnet/minecraft/network/packet/s2c/play/WorldBorderWarningBlocksChangedS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldBorderWarningBlocksChangedS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderWarningBlocksChangedS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, WorldBorderWarningBlocksChangedS2CPacket> CODEC = Packet.createCodec(WorldBorderWarningBlocksChangedS2CPacket::write, WorldBorderWarningBlocksChangedS2CPacket::new);
    private final int warningBlocks;

    public WorldBorderWarningBlocksChangedS2CPacket(WorldBorder worldBorder) {
        this.warningBlocks = worldBorder.getWarningBlocks();
    }

    private WorldBorderWarningBlocksChangedS2CPacket(PacketByteBuf buf) {
        this.warningBlocks = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.warningBlocks);
    }

    @Override
    public PacketType<WorldBorderWarningBlocksChangedS2CPacket> getPacketType() {
        return PlayPackets.SET_BORDER_WARNING_DISTANCE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldBorderWarningBlocksChanged(this);
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }
}

