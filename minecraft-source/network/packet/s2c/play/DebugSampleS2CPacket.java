/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readLongArray()[J
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/PacketByteBuf;writeLongArray([J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onDebugSample(Lnet/minecraft/network/packet/s2c/play/DebugSampleS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/DebugSampleS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.profiler.log.DebugSampleType;

public record DebugSampleS2CPacket(long[] sample, DebugSampleType debugSampleType) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, DebugSampleS2CPacket> CODEC = Packet.createCodec(DebugSampleS2CPacket::write, DebugSampleS2CPacket::new);

    private DebugSampleS2CPacket(PacketByteBuf buf) {
        this(buf.readLongArray(), buf.readEnumConstant(DebugSampleType.class));
    }

    private void write(PacketByteBuf buf) {
        buf.writeLongArray(this.sample);
        buf.writeEnumConstant(this.debugSampleType);
    }

    @Override
    public PacketType<DebugSampleS2CPacket> getPacketType() {
        return PlayPackets.DEBUG_SAMPLE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onDebugSample(this);
    }
}

