/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onBundleItemSelected(Lnet/minecraft/network/packet/c2s/play/BundleItemSelectedC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/BundleItemSelectedC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record BundleItemSelectedC2SPacket(int slotId, int selectedItemIndex) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, BundleItemSelectedC2SPacket> CODEC = Packet.createCodec(BundleItemSelectedC2SPacket::write, BundleItemSelectedC2SPacket::new);

    private BundleItemSelectedC2SPacket(PacketByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
        if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
            throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
        }
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.slotId);
        buf.writeVarInt(this.selectedItemIndex);
    }

    @Override
    public PacketType<BundleItemSelectedC2SPacket> getPacketType() {
        return PlayPackets.BUNDLE_ITEM_SELECTED;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onBundleItemSelected(this);
    }
}

