/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onTitleClear(Lnet/minecraft/network/packet/s2c/play/ClearTitleS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/ClearTitleS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class ClearTitleS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, ClearTitleS2CPacket> CODEC = Packet.createCodec(ClearTitleS2CPacket::write, ClearTitleS2CPacket::new);
    private final boolean reset;

    public ClearTitleS2CPacket(boolean reset) {
        this.reset = reset;
    }

    private ClearTitleS2CPacket(PacketByteBuf buf) {
        this.reset = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeBoolean(this.reset);
    }

    @Override
    public PacketType<ClearTitleS2CPacket> getPacketType() {
        return PlayPackets.CLEAR_TITLES;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onTitleClear(this);
    }

    public boolean shouldReset() {
        return this.reset;
    }
}

