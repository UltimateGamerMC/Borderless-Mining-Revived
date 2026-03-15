/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeDouble(D)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onProjectilePower(Lnet/minecraft/network/packet/s2c/play/ProjectilePowerS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ProjectilePowerS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class ProjectilePowerS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, ProjectilePowerS2CPacket> CODEC = Packet.createCodec(ProjectilePowerS2CPacket::write, ProjectilePowerS2CPacket::new);
    private final int entityId;
    private final double accelerationPower;

    public ProjectilePowerS2CPacket(int entityId, double accelerationPower) {
        this.entityId = entityId;
        this.accelerationPower = accelerationPower;
    }

    private ProjectilePowerS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.accelerationPower = buf.readDouble();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeDouble(this.accelerationPower);
    }

    @Override
    public PacketType<ProjectilePowerS2CPacket> getPacketType() {
        return PlayPackets.PROJECTILE_POWER;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onProjectilePower(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getAccelerationPower() {
        return this.accelerationPower;
    }
}

