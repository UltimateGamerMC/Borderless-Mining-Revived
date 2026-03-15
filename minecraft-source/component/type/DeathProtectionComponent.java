/*
 * External method calls:
 *   Lnet/minecraft/item/consume/ConsumeEffect;onConsume(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Z
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record DeathProtectionComponent(List<ConsumeEffect> deathEffects) {
    public static final Codec<DeathProtectionComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(ConsumeEffect.CODEC.listOf().optionalFieldOf("death_effects", List.of()).forGetter(DeathProtectionComponent::deathEffects)).apply((Applicative<DeathProtectionComponent, ?>)instance, DeathProtectionComponent::new));
    public static final PacketCodec<RegistryByteBuf, DeathProtectionComponent> PACKET_CODEC = PacketCodec.tuple(ConsumeEffect.PACKET_CODEC.collect(PacketCodecs.toList()), DeathProtectionComponent::deathEffects, DeathProtectionComponent::new);
    public static final DeathProtectionComponent TOTEM_OF_UNDYING = new DeathProtectionComponent(List.of(new ClearAllEffectsConsumeEffect(), new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1), new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1), new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0)))));

    public void applyDeathEffects(ItemStack stack, LivingEntity entity) {
        for (ConsumeEffect lv : this.deathEffects) {
            lv.onConsume(entity.getEntityWorld(), stack, entity);
        }
    }
}

