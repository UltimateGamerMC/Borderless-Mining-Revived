/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSetCursorItem(Lnet/minecraft/network/packet/s2c/play/SetCursorItemS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SetCursorItemS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record SetCursorItemS2CPacket(ItemStack contents) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, SetCursorItemS2CPacket> CODEC = PacketCodec.tuple(ItemStack.OPTIONAL_PACKET_CODEC, SetCursorItemS2CPacket::contents, SetCursorItemS2CPacket::new);

    @Override
    public PacketType<SetCursorItemS2CPacket> getPacketType() {
        return PlayPackets.SET_CURSOR_ITEM;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSetCursorItem(this);
    }
}

