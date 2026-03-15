/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onTickStep(Lnet/minecraft/network/packet/s2c/play/TickStepS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/TickStepS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.tick.TickManager;

public record TickStepS2CPacket(int tickSteps) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, TickStepS2CPacket> CODEC = Packet.createCodec(TickStepS2CPacket::write, TickStepS2CPacket::new);

    private TickStepS2CPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    public static TickStepS2CPacket create(TickManager tickManager) {
        return new TickStepS2CPacket(tickManager.getStepTicks());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.tickSteps);
    }

    @Override
    public PacketType<TickStepS2CPacket> getPacketType() {
        return PlayPackets.TICKING_STEP;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onTickStep(this);
    }
}

