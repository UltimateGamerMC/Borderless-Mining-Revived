/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onUpdateSelectedSlot(Lnet/minecraft/network/packet/s2c/play/UpdateSelectedSlotS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/UpdateSelectedSlotS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record UpdateSelectedSlotS2CPacket(int slot) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<ByteBuf, UpdateSelectedSlotS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, UpdateSelectedSlotS2CPacket::slot, UpdateSelectedSlotS2CPacket::new);

    @Override
    public PacketType<UpdateSelectedSlotS2CPacket> getPacketType() {
        return PlayPackets.SET_CARRIED_ITEM_S2C;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onUpdateSelectedSlot(this);
    }
}

