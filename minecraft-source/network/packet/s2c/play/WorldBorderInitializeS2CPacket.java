/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeDouble(D)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onWorldBorderInitialize(Lnet/minecraft/network/packet/s2c/play/WorldBorderInitializeS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/WorldBorderInitializeS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderInitializeS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, WorldBorderInitializeS2CPacket> CODEC = Packet.createCodec(WorldBorderInitializeS2CPacket::write, WorldBorderInitializeS2CPacket::new);
    private final double centerX;
    private final double centerZ;
    private final double size;
    private final double sizeLerpTarget;
    private final long sizeLerpTime;
    private final int maxRadius;
    private final int warningBlocks;
    private final int warningTime;

    private WorldBorderInitializeS2CPacket(PacketByteBuf buf) {
        this.centerX = buf.readDouble();
        this.centerZ = buf.readDouble();
        this.size = buf.readDouble();
        this.sizeLerpTarget = buf.readDouble();
        this.sizeLerpTime = buf.readVarLong();
        this.maxRadius = buf.readVarInt();
        this.warningBlocks = buf.readVarInt();
        this.warningTime = buf.readVarInt();
    }

    public WorldBorderInitializeS2CPacket(WorldBorder worldBorder) {
        this.centerX = worldBorder.getCenterX();
        this.centerZ = worldBorder.getCenterZ();
        this.size = worldBorder.getSize();
        this.sizeLerpTarget = worldBorder.getSizeLerpTarget();
        this.sizeLerpTime = worldBorder.getSizeLerpTime();
        this.maxRadius = worldBorder.getMaxRadius();
        this.warningBlocks = worldBorder.getWarningBlocks();
        this.warningTime = worldBorder.getWarningTime();
    }

    private void write(PacketByteBuf buf) {
        buf.writeDouble(this.centerX);
        buf.writeDouble(this.centerZ);
        buf.writeDouble(this.size);
        buf.writeDouble(this.sizeLerpTarget);
        buf.writeVarLong(this.sizeLerpTime);
        buf.writeVarInt(this.maxRadius);
        buf.writeVarInt(this.warningBlocks);
        buf.writeVarInt(this.warningTime);
    }

    @Override
    public PacketType<WorldBorderInitializeS2CPacket> getPacketType() {
        return PlayPackets.INITIALIZE_BORDER;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onWorldBorderInitialize(this);
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public double getSizeLerpTarget() {
        return this.sizeLerpTarget;
    }

    public double getSize() {
        return this.size;
    }

    public long getSizeLerpTime() {
        return this.sizeLerpTime;
    }

    public int getMaxRadius() {
        return this.maxRadius;
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }
}

