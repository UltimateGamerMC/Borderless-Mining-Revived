/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeFloat(F)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onUpdateTickRate(Lnet/minecraft/network/packet/s2c/play/UpdateTickRateS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/UpdateTickRateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.tick.TickManager;

public record UpdateTickRateS2CPacket(float tickRate, boolean isFrozen) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, UpdateTickRateS2CPacket> CODEC = Packet.createCodec(UpdateTickRateS2CPacket::write, UpdateTickRateS2CPacket::new);

    private UpdateTickRateS2CPacket(PacketByteBuf buf) {
        this(buf.readFloat(), buf.readBoolean());
    }

    public static UpdateTickRateS2CPacket create(TickManager tickManager) {
        return new UpdateTickRateS2CPacket(tickManager.getTickRate(), tickManager.isFrozen());
    }

    private void write(PacketByteBuf buf) {
        buf.writeFloat(this.tickRate);
        buf.writeBoolean(this.isFrozen);
    }

    @Override
    public PacketType<UpdateTickRateS2CPacket> getPacketType() {
        return PlayPackets.TICKING_STATE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onUpdateTickRate(this);
    }
}

