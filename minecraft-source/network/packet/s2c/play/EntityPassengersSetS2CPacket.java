/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readIntArray()[I
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeIntArray([I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntityPassengersSet(Lnet/minecraft/network/packet/s2c/play/EntityPassengersSetS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntityPassengersSetS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class EntityPassengersSetS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, EntityPassengersSetS2CPacket> CODEC = Packet.createCodec(EntityPassengersSetS2CPacket::write, EntityPassengersSetS2CPacket::new);
    private final int entityId;
    private final int[] passengerIds;

    public EntityPassengersSetS2CPacket(Entity entity) {
        this.entityId = entity.getId();
        List<Entity> list = entity.getPassengerList();
        this.passengerIds = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            this.passengerIds[i] = list.get(i).getId();
        }
    }

    private EntityPassengersSetS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.passengerIds = buf.readIntArray();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeIntArray(this.passengerIds);
    }

    @Override
    public PacketType<EntityPassengersSetS2CPacket> getPacketType() {
        return PlayPackets.SET_PASSENGERS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntityPassengersSet(this);
    }

    public int[] getPassengerIds() {
        return this.passengerIds;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

