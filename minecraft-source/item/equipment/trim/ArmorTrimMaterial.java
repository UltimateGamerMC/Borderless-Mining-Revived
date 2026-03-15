/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/registry/entry/RegistryElementCodec;of(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lnet/minecraft/registry/entry/RegistryElementCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.item.equipment.trim;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.equipment.trim.ArmorTrimAssets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record ArmorTrimMaterial(ArmorTrimAssets assets, Text description) {
    public static final Codec<ArmorTrimMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(ArmorTrimAssets.CODEC.forGetter(ArmorTrimMaterial::assets), ((MapCodec)TextCodecs.CODEC.fieldOf("description")).forGetter(ArmorTrimMaterial::description)).apply((Applicative<ArmorTrimMaterial, ?>)instance, ArmorTrimMaterial::new));
    public static final PacketCodec<RegistryByteBuf, ArmorTrimMaterial> PACKET_CODEC = PacketCodec.tuple(ArmorTrimAssets.PACKET_CODEC, ArmorTrimMaterial::assets, TextCodecs.REGISTRY_PACKET_CODEC, ArmorTrimMaterial::description, ArmorTrimMaterial::new);
    public static final Codec<RegistryEntry<ArmorTrimMaterial>> ENTRY_CODEC = RegistryElementCodec.of(RegistryKeys.TRIM_MATERIAL, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<ArmorTrimMaterial>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.TRIM_MATERIAL, PACKET_CODEC);
}

