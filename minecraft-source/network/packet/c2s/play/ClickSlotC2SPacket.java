/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onClickSlot(Lnet/minecraft/network/packet/c2s/play/ClickSlotC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;map(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketCodec;Lnet/minecraft/network/codec/PacketCodec;I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function7;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/ClickSlotC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;

public record ClickSlotC2SPacket(int syncId, int revision, short slot, byte button, SlotActionType actionType, Int2ObjectMap<ItemStackHash> modifiedStacks, ItemStackHash cursor) implements Packet<ServerPlayPacketListener>
{
    private static final int MAX_MODIFIED_STACKS = 128;
    private static final PacketCodec<RegistryByteBuf, Int2ObjectMap<ItemStackHash>> STACK_MAP_CODEC = PacketCodecs.map(Int2ObjectOpenHashMap::new, PacketCodecs.SHORT.xmap(Short::intValue, Integer::shortValue), ItemStackHash.PACKET_CODEC, 128);
    public static final PacketCodec<RegistryByteBuf, ClickSlotC2SPacket> CODEC = PacketCodec.tuple(PacketCodecs.SYNC_ID, ClickSlotC2SPacket::syncId, PacketCodecs.VAR_INT, ClickSlotC2SPacket::revision, PacketCodecs.SHORT, ClickSlotC2SPacket::slot, PacketCodecs.BYTE, ClickSlotC2SPacket::button, SlotActionType.PACKET_CODEC, ClickSlotC2SPacket::actionType, STACK_MAP_CODEC, ClickSlotC2SPacket::modifiedStacks, ItemStackHash.PACKET_CODEC, ClickSlotC2SPacket::cursor, ClickSlotC2SPacket::new);

    public ClickSlotC2SPacket {
        modifiedStacks = Int2ObjectMaps.unmodifiable(modifiedStacks);
    }

    @Override
    public PacketType<ClickSlotC2SPacket> getPacketType() {
        return PlayPackets.CONTAINER_CLICK;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onClickSlot(this);
    }
}

