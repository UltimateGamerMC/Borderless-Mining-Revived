/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onBlockValueDebug(Lnet/minecraft/network/packet/s2c/play/BlockValueDebugS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/BlockValueDebugS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.debug.DebugSubscriptionType;

public record BlockValueDebugS2CPacket(BlockPos blockPos, DebugSubscriptionType.OptionalValue<?> update) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, BlockValueDebugS2CPacket> PACKET_CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockValueDebugS2CPacket::blockPos, DebugSubscriptionType.OptionalValue.PACKET_CODEC, BlockValueDebugS2CPacket::update, BlockValueDebugS2CPacket::new);

    @Override
    public PacketType<BlockValueDebugS2CPacket> getPacketType() {
        return PlayPackets.BLOCK_VALUE_DEBUG;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onBlockValueDebug(this);
    }
}

