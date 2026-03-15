/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSetPlayerInventory(Lnet/minecraft/network/packet/s2c/play/SetPlayerInventoryS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SetPlayerInventoryS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record SetPlayerInventoryS2CPacket(int slot, ItemStack contents) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, SetPlayerInventoryS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, SetPlayerInventoryS2CPacket::slot, ItemStack.OPTIONAL_PACKET_CODEC, SetPlayerInventoryS2CPacket::contents, SetPlayerInventoryS2CPacket::new);

    @Override
    public PacketType<SetPlayerInventoryS2CPacket> getPacketType() {
        return PlayPackets.SET_PLAYER_INVENTORY;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSetPlayerInventory(this);
    }
}

