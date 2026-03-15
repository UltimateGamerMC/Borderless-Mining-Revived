/*
 * External method calls:
 *   Lnet/minecraft/entity/data/DataTracker$SerializedEntry;write(Lnet/minecraft/network/RegistryByteBuf;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/entity/data/DataTracker$SerializedEntry;fromBuf(Lnet/minecraft/network/RegistryByteBuf;I)Lnet/minecraft/entity/data/DataTracker$SerializedEntry;
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntityTrackerUpdate(Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;read(Lnet/minecraft/network/RegistryByteBuf;)Ljava/util/List;
 *   Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;write(Ljava/util/List;Lnet/minecraft/network/RegistryByteBuf;)V
 *   Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record EntityTrackerUpdateS2CPacket(int id, List<DataTracker.SerializedEntry<?>> trackedValues) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, EntityTrackerUpdateS2CPacket> CODEC = Packet.createCodec(EntityTrackerUpdateS2CPacket::write, EntityTrackerUpdateS2CPacket::new);
    public static final int MARKER_ID = 255;

    private EntityTrackerUpdateS2CPacket(RegistryByteBuf buf) {
        this(buf.readVarInt(), EntityTrackerUpdateS2CPacket.read(buf));
    }

    private static void write(List<DataTracker.SerializedEntry<?>> trackedValues, RegistryByteBuf buf) {
        for (DataTracker.SerializedEntry<?> lv : trackedValues) {
            lv.write(buf);
        }
        buf.writeByte(255);
    }

    private static List<DataTracker.SerializedEntry<?>> read(RegistryByteBuf buf) {
        short i;
        ArrayList list = new ArrayList();
        while ((i = buf.readUnsignedByte()) != 255) {
            list.add(DataTracker.SerializedEntry.fromBuf(buf, i));
        }
        return list;
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.id);
        EntityTrackerUpdateS2CPacket.write(this.trackedValues, buf);
    }

    @Override
    public PacketType<EntityTrackerUpdateS2CPacket> getPacketType() {
        return PlayPackets.SET_ENTITY_DATA;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntityTrackerUpdate(this);
    }
}

