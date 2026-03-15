/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;writeSyncId(I)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeShort(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onScreenHandlerSlotUpdate(Lnet/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class ScreenHandlerSlotUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, ScreenHandlerSlotUpdateS2CPacket> CODEC = Packet.createCodec(ScreenHandlerSlotUpdateS2CPacket::write, ScreenHandlerSlotUpdateS2CPacket::new);
    private final int syncId;
    private final int revision;
    private final int slot;
    private final ItemStack stack;

    public ScreenHandlerSlotUpdateS2CPacket(int syncId, int revision, int slot, ItemStack stack) {
        this.syncId = syncId;
        this.revision = revision;
        this.slot = slot;
        this.stack = stack.copy();
    }

    private ScreenHandlerSlotUpdateS2CPacket(RegistryByteBuf buf) {
        this.syncId = buf.readSyncId();
        this.revision = buf.readVarInt();
        this.slot = buf.readShort();
        this.stack = (ItemStack)ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
    }

    private void write(RegistryByteBuf buf) {
        buf.writeSyncId(this.syncId);
        buf.writeVarInt(this.revision);
        buf.writeShort(this.slot);
        ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, this.stack);
    }

    @Override
    public PacketType<ScreenHandlerSlotUpdateS2CPacket> getPacketType() {
        return PlayPackets.CONTAINER_SET_SLOT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onScreenHandlerSlotUpdate(this);
    }

    public int getSyncId() {
        return this.syncId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int getRevision() {
        return this.revision;
    }
}

