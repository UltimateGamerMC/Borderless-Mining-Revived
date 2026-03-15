/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onTestInstanceBlockStatus(Lnet/minecraft/network/packet/s2c/play/TestInstanceBlockStatusS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;optional(Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/TestInstanceBlockStatusS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Optional;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.Vec3i;

public record TestInstanceBlockStatusS2CPacket(Text status, Optional<Vec3i> size) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, TestInstanceBlockStatusS2CPacket> CODEC = PacketCodec.tuple(TextCodecs.REGISTRY_PACKET_CODEC, TestInstanceBlockStatusS2CPacket::status, PacketCodecs.optional(Vec3i.PACKET_CODEC), TestInstanceBlockStatusS2CPacket::size, TestInstanceBlockStatusS2CPacket::new);

    @Override
    public PacketType<TestInstanceBlockStatusS2CPacket> getPacketType() {
        return PlayPackets.TEST_INSTANCE_BLOCK_STATUS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onTestInstanceBlockStatus(this);
    }
}

