/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeDouble(D)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldBorderSizeChanged(Lnet/minecraft/network/packet/s2c/play/WorldBorderSizeChangedS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldBorderSizeChangedS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderSizeChangedS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, WorldBorderSizeChangedS2CPacket> CODEC = Packet.createCodec(WorldBorderSizeChangedS2CPacket::write, WorldBorderSizeChangedS2CPacket::new);
    private final double sizeLerpTarget;

    public WorldBorderSizeChangedS2CPacket(WorldBorder worldBorder) {
        this.sizeLerpTarget = worldBorder.getSizeLerpTarget();
    }

    private WorldBorderSizeChangedS2CPacket(PacketByteBuf buf) {
        this.sizeLerpTarget = buf.readDouble();
    }

    private void write(PacketByteBuf buf) {
        buf.writeDouble(this.sizeLerpTarget);
    }

    @Override
    public PacketType<WorldBorderSizeChangedS2CPacket> getPacketType() {
        return PlayPackets.SET_BORDER_SIZE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldBorderSizeChanged(this);
    }

    public double getSizeLerpTarget() {
        return this.sizeLerpTarget;
    }
}

