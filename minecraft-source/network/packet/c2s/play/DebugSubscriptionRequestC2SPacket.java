/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onDebugSubscriptionRequest(Lnet/minecraft/network/packet/c2s/play/DebugSubscriptionRequestC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;toCollection(Ljava/util/function/IntFunction;)Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/DebugSubscriptionRequestC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.debug.DebugSubscriptionType;

public record DebugSubscriptionRequestC2SPacket(Set<DebugSubscriptionType<?>> subscriptions) implements Packet<ServerPlayPacketListener>
{
    private static final PacketCodec<RegistryByteBuf, Set<DebugSubscriptionType<?>>> TYPE_SET_CODEC = PacketCodecs.registryValue(RegistryKeys.DEBUG_SUBSCRIPTION).collect(PacketCodecs.toCollection(ReferenceOpenHashSet::new));
    public static final PacketCodec<RegistryByteBuf, DebugSubscriptionRequestC2SPacket> CODEC = TYPE_SET_CODEC.xmap(DebugSubscriptionRequestC2SPacket::new, DebugSubscriptionRequestC2SPacket::subscriptions);

    @Override
    public PacketType<DebugSubscriptionRequestC2SPacket> getPacketType() {
        return PlayPackets.DEBUG_SUBSCRIPTION_REQUEST;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onDebugSubscriptionRequest(this);
    }
}

