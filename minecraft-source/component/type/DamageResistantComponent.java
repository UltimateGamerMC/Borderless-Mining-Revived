/*
 * External method calls:
 *   Lnet/minecraft/registry/tag/TagKey;codec(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/registry/tag/TagKey;packetCodec(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record DamageResistantComponent(TagKey<DamageType> types) {
    public static final Codec<DamageResistantComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)TagKey.codec(RegistryKeys.DAMAGE_TYPE).fieldOf("types")).forGetter(DamageResistantComponent::types)).apply((Applicative<DamageResistantComponent, ?>)instance, DamageResistantComponent::new));
    public static final PacketCodec<RegistryByteBuf, DamageResistantComponent> PACKET_CODEC = PacketCodec.tuple(TagKey.packetCodec(RegistryKeys.DAMAGE_TYPE), DamageResistantComponent::types, DamageResistantComponent::new);

    public boolean resists(DamageSource damageSource) {
        return damageSource.isIn(this.types);
    }
}

