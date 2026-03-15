/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntityStatus(Lnet/minecraft/network/packet/s2c/play/EntityStatusS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntityStatusS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class EntityStatusS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, EntityStatusS2CPacket> CODEC = Packet.createCodec(EntityStatusS2CPacket::write, EntityStatusS2CPacket::new);
    private final int entityId;
    private final byte status;

    public EntityStatusS2CPacket(Entity entity, byte status) {
        this.entityId = entity.getId();
        this.status = status;
    }

    private EntityStatusS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.status = buf.readByte();
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.status);
    }

    @Override
    public PacketType<EntityStatusS2CPacket> getPacketType() {
        return PlayPackets.ENTITY_EVENT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntityStatus(this);
    }

    public @Nullable Entity getEntity(World world) {
        return world.getEntityById(this.entityId);
    }

    public byte getStatus() {
        return this.status;
    }
}

