/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onStatistics(Lnet/minecraft/network/packet/s2c/play/StatisticsS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;map(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketCodec;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/StatisticsS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.stat.Stat;

public record StatisticsS2CPacket(Object2IntMap<Stat<?>> stats) implements Packet<ClientPlayPacketListener>
{
    private static final PacketCodec<RegistryByteBuf, Object2IntMap<Stat<?>>> STAT_MAP_CODEC = PacketCodecs.map(Object2IntOpenHashMap::new, Stat.PACKET_CODEC, PacketCodecs.VAR_INT);
    public static final PacketCodec<RegistryByteBuf, StatisticsS2CPacket> CODEC = STAT_MAP_CODEC.xmap(StatisticsS2CPacket::new, StatisticsS2CPacket::stats);

    @Override
    public PacketType<StatisticsS2CPacket> getPacketType() {
        return PlayPackets.AWARD_STATS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onStatistics(this);
    }
}

