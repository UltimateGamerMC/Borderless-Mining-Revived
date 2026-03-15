/*
 * External method calls:
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimMaterial;description()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/MutableText;fillStyle(Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/registry/entry/RegistryElementCodec;of(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lnet/minecraft/registry/entry/RegistryElementCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.item.equipment.trim;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public record ArmorTrimPattern(Identifier assetId, Text description, boolean decal) {
    public static final Codec<ArmorTrimPattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("asset_id")).forGetter(ArmorTrimPattern::assetId), ((MapCodec)TextCodecs.CODEC.fieldOf("description")).forGetter(ArmorTrimPattern::description), ((MapCodec)Codec.BOOL.fieldOf("decal")).orElse(false).forGetter(ArmorTrimPattern::decal)).apply((Applicative<ArmorTrimPattern, ?>)instance, ArmorTrimPattern::new));
    public static final PacketCodec<RegistryByteBuf, ArmorTrimPattern> PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, ArmorTrimPattern::assetId, TextCodecs.REGISTRY_PACKET_CODEC, ArmorTrimPattern::description, PacketCodecs.BOOLEAN, ArmorTrimPattern::decal, ArmorTrimPattern::new);
    public static final Codec<RegistryEntry<ArmorTrimPattern>> ENTRY_CODEC = RegistryElementCodec.of(RegistryKeys.TRIM_PATTERN, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<ArmorTrimPattern>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.TRIM_PATTERN, PACKET_CODEC);

    public Text getDescription(RegistryEntry<ArmorTrimMaterial> material) {
        return this.description.copy().fillStyle(material.value().description().getStyle());
    }
}

