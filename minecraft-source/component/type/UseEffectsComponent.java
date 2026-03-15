/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record UseEffectsComponent(boolean canSprint, boolean interactVibrations, float speedMultiplier) {
    public static final UseEffectsComponent DEFAULT = new UseEffectsComponent(false, true, 0.2f);
    public static final Codec<UseEffectsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("can_sprint", UseEffectsComponent.DEFAULT.canSprint).forGetter(UseEffectsComponent::canSprint), Codec.BOOL.optionalFieldOf("interact_vibrations", UseEffectsComponent.DEFAULT.interactVibrations).forGetter(UseEffectsComponent::interactVibrations), Codec.floatRange(0.0f, 1.0f).optionalFieldOf("speed_multiplier", Float.valueOf(UseEffectsComponent.DEFAULT.speedMultiplier)).forGetter(UseEffectsComponent::speedMultiplier)).apply((Applicative<UseEffectsComponent, ?>)instance, UseEffectsComponent::new));
    public static final PacketCodec<ByteBuf, UseEffectsComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, UseEffectsComponent::canSprint, PacketCodecs.BOOLEAN, UseEffectsComponent::interactVibrations, PacketCodecs.FLOAT, UseEffectsComponent::speedMultiplier, UseEffectsComponent::new);
}

