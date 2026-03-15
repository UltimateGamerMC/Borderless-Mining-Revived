/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.SwingAnimationType;
import net.minecraft.util.dynamic.Codecs;

public record SwingAnimationComponent(SwingAnimationType type, int duration) {
    public static final SwingAnimationComponent DEFAULT = new SwingAnimationComponent(SwingAnimationType.WHACK, 6);
    public static final Codec<SwingAnimationComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(SwingAnimationType.CODEC.optionalFieldOf("type", SwingAnimationComponent.DEFAULT.type).forGetter(SwingAnimationComponent::type), Codecs.POSITIVE_INT.optionalFieldOf("duration", SwingAnimationComponent.DEFAULT.duration).forGetter(SwingAnimationComponent::duration)).apply((Applicative<SwingAnimationComponent, ?>)instance, SwingAnimationComponent::new));
    public static final PacketCodec<ByteBuf, SwingAnimationComponent> PACKET_CODEC = PacketCodec.tuple(SwingAnimationType.PACKET_CODEC, SwingAnimationComponent::type, PacketCodecs.VAR_INT, SwingAnimationComponent::duration, SwingAnimationComponent::new);
}

