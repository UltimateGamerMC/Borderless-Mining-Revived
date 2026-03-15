package net.minecraft.world.attribute.timeline;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;

public record Keyframe<T>(int ticks, T value) {
    public static <T> Codec<Keyframe<T>> createCodec(Codec<T> valueCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codecs.NON_NEGATIVE_INT.fieldOf("ticks")).forGetter(Keyframe::ticks), ((MapCodec)valueCodec.fieldOf("value")).forGetter(Keyframe::value)).apply((Applicative<Keyframe, ?>)instance, Keyframe::new));
    }
}

