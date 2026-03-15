/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onInventory(Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record InventoryS2CPacket(int syncId, int revision, List<ItemStack> contents, ItemStack cursorStack) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, InventoryS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.SYNC_ID, InventoryS2CPacket::syncId, PacketCodecs.VAR_INT, InventoryS2CPacket::revision, ItemStack.OPTIONAL_LIST_PACKET_CODEC, InventoryS2CPacket::contents, ItemStack.OPTIONAL_PACKET_CODEC, InventoryS2CPacket::cursorStack, InventoryS2CPacket::new);

    @Override
    public PacketType<InventoryS2CPacket> getPacketType() {
        return PlayPackets.CONTAINER_SET_CONTENT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onInventory(this);
    }
}

