/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntityPositionSync(Lnet/minecraft/network/packet/s2c/play/EntityPositionSyncS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntityPositionSyncS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record EntityPositionSyncS2CPacket(int id, EntityPosition values, boolean onGround) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, EntityPositionSyncS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, EntityPositionSyncS2CPacket::id, EntityPosition.PACKET_CODEC, EntityPositionSyncS2CPacket::values, PacketCodecs.BOOLEAN, EntityPositionSyncS2CPacket::onGround, EntityPositionSyncS2CPacket::new);

    public static EntityPositionSyncS2CPacket create(Entity entity) {
        return new EntityPositionSyncS2CPacket(entity.getId(), new EntityPosition(entity.getSyncedPos(), entity.getVelocity(), entity.getYaw(), entity.getPitch()), entity.isOnGround());
    }

    @Override
    public PacketType<EntityPositionSyncS2CPacket> getPacketType() {
        return PlayPackets.ENTITY_POSITION_SYNC;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntityPositionSync(this);
    }
}

