/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntityAttach(Lnet/minecraft/network/packet/s2c/play/EntityAttachS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntityAttachS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import org.jspecify.annotations.Nullable;

public class EntityAttachS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, EntityAttachS2CPacket> CODEC = Packet.createCodec(EntityAttachS2CPacket::write, EntityAttachS2CPacket::new);
    private final int attachedEntityId;
    private final int holdingEntityId;

    public EntityAttachS2CPacket(Entity attachedEntity, @Nullable Entity holdingEntity) {
        this.attachedEntityId = attachedEntity.getId();
        this.holdingEntityId = holdingEntity != null ? holdingEntity.getId() : 0;
    }

    private EntityAttachS2CPacket(PacketByteBuf buf) {
        this.attachedEntityId = buf.readInt();
        this.holdingEntityId = buf.readInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.attachedEntityId);
        buf.writeInt(this.holdingEntityId);
    }

    @Override
    public PacketType<EntityAttachS2CPacket> getPacketType() {
        return PlayPackets.SET_ENTITY_LINK;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntityAttach(this);
    }

    public int getAttachedEntityId() {
        return this.attachedEntityId;
    }

    public int getHoldingEntityId() {
        return this.holdingEntityId;
    }
}

