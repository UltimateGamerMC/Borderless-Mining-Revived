/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeShort(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onUpdateSelectedSlot(Lnet/minecraft/network/packet/c2s/play/UpdateSelectedSlotC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/UpdateSelectedSlotC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class UpdateSelectedSlotC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, UpdateSelectedSlotC2SPacket> CODEC = Packet.createCodec(UpdateSelectedSlotC2SPacket::write, UpdateSelectedSlotC2SPacket::new);
    private final int selectedSlot;

    public UpdateSelectedSlotC2SPacket(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    private UpdateSelectedSlotC2SPacket(PacketByteBuf buf) {
        this.selectedSlot = buf.readShort();
    }

    private void write(PacketByteBuf buf) {
        buf.writeShort(this.selectedSlot);
    }

    @Override
    public PacketType<UpdateSelectedSlotC2SPacket> getPacketType() {
        return PlayPackets.SET_CARRIED_ITEM_C2S;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onUpdateSelectedSlot(this);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }
}

