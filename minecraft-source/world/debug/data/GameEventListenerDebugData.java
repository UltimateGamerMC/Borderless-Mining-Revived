/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.world.debug.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record GameEventListenerDebugData(int listenerRadius) {
    public static final PacketCodec<RegistryByteBuf, GameEventListenerDebugData> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, GameEventListenerDebugData::listenerRadius, GameEventListenerDebugData::new);
}

