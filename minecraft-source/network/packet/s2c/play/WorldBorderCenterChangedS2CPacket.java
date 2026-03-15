/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeDouble(D)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldBorderCenterChanged(Lnet/minecraft/network/packet/s2c/play/WorldBorderCenterChangedS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldBorderCenterChangedS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderCenterChangedS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, WorldBorderCenterChangedS2CPacket> CODEC = Packet.createCodec(WorldBorderCenterChangedS2CPacket::write, WorldBorderCenterChangedS2CPacket::new);
    private final double centerX;
    private final double centerZ;

    public WorldBorderCenterChangedS2CPacket(WorldBorder worldBorder) {
        this.centerX = worldBorder.getCenterX();
        this.centerZ = worldBorder.getCenterZ();
    }

    private WorldBorderCenterChangedS2CPacket(PacketByteBuf buf) {
        this.centerX = buf.readDouble();
        this.centerZ = buf.readDouble();
    }

    private void write(PacketByteBuf buf) {
        buf.writeDouble(this.centerX);
        buf.writeDouble(this.centerZ);
    }

    @Override
    public PacketType<WorldBorderCenterChangedS2CPacket> getPacketType() {
        return PlayPackets.SET_BORDER_CENTER;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldBorderCenterChanged(this);
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public double getCenterX() {
        return this.centerX;
    }
}

