/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readIntList()Lit/unimi/dsi/fastutil/ints/IntList;
 *   Lnet/minecraft/network/PacketByteBuf;writeIntList(Lit/unimi/dsi/fastutil/ints/IntList;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntitiesDestroy(Lnet/minecraft/network/packet/s2c/play/EntitiesDestroyS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntitiesDestroyS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class EntitiesDestroyS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, EntitiesDestroyS2CPacket> CODEC = Packet.createCodec(EntitiesDestroyS2CPacket::write, EntitiesDestroyS2CPacket::new);
    private final IntList entityIds;

    public EntitiesDestroyS2CPacket(IntList entityIds) {
        this.entityIds = new IntArrayList(entityIds);
    }

    public EntitiesDestroyS2CPacket(int ... entityIds) {
        this.entityIds = new IntArrayList(entityIds);
    }

    private EntitiesDestroyS2CPacket(PacketByteBuf buf) {
        this.entityIds = buf.readIntList();
    }

    private void write(PacketByteBuf buf) {
        buf.writeIntList(this.entityIds);
    }

    @Override
    public PacketType<EntitiesDestroyS2CPacket> getPacketType() {
        return PlayPackets.REMOVE_ENTITIES;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntitiesDestroy(this);
    }

    public IntList getEntityIds() {
        return this.entityIds;
    }
}

