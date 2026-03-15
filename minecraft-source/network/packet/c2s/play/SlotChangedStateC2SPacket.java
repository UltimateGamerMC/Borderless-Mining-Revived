/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeSyncId(I)V
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onSlotChangedState(Lnet/minecraft/network/packet/c2s/play/SlotChangedStateC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/SlotChangedStateC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record SlotChangedStateC2SPacket(int slotId, int screenHandlerId, boolean newState) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, SlotChangedStateC2SPacket> CODEC = Packet.createCodec(SlotChangedStateC2SPacket::write, SlotChangedStateC2SPacket::new);

    private SlotChangedStateC2SPacket(PacketByteBuf buf) {
        this(buf.readVarInt(), buf.readSyncId(), buf.readBoolean());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.slotId);
        buf.writeSyncId(this.screenHandlerId);
        buf.writeBoolean(this.newState);
    }

    @Override
    public PacketType<SlotChangedStateC2SPacket> getPacketType() {
        return PlayPackets.CONTAINER_SLOT_STATE_CHANGED;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onSlotChangedState(this);
    }
}

