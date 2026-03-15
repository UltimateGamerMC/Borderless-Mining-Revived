/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSimulationDistance(Lnet/minecraft/network/packet/s2c/play/SimulationDistanceS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SimulationDistanceS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record SimulationDistanceS2CPacket(int simulationDistance) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, SimulationDistanceS2CPacket> CODEC = Packet.createCodec(SimulationDistanceS2CPacket::write, SimulationDistanceS2CPacket::new);

    private SimulationDistanceS2CPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.simulationDistance);
    }

    @Override
    public PacketType<SimulationDistanceS2CPacket> getPacketType() {
        return PlayPackets.SET_SIMULATION_DISTANCE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSimulationDistance(this);
    }
}

