/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.world.debug.data;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;

public record BeeHiveDebugData(Block type, int occupantCount, int honeyLevel, boolean sedated) {
    public static final PacketCodec<RegistryByteBuf, BeeHiveDebugData> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.registryValue(RegistryKeys.BLOCK), BeeHiveDebugData::type, PacketCodecs.VAR_INT, BeeHiveDebugData::occupantCount, PacketCodecs.VAR_INT, BeeHiveDebugData::honeyLevel, PacketCodecs.BOOLEAN, BeeHiveDebugData::sedated, BeeHiveDebugData::new);

    public static BeeHiveDebugData fromBeehive(BeehiveBlockEntity beehive) {
        return new BeeHiveDebugData(beehive.getCachedState().getBlock(), beehive.getBeeCount(), BeehiveBlockEntity.getHoneyLevel(beehive.getCachedState()), beehive.isSmoked());
    }
}

