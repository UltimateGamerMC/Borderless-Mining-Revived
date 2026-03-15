package net.minecraft.world.attribute;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BlendArgument(float value, float alpha) {
    private static final Codec<BlendArgument> INTERNAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("value")).forGetter(BlendArgument::value), Codec.floatRange(0.0f, 1.0f).optionalFieldOf("alpha", Float.valueOf(1.0f)).forGetter(BlendArgument::alpha)).apply((Applicative<BlendArgument, ?>)instance, BlendArgument::new));
    public static final Codec<BlendArgument> CODEC = Codec.either(Codec.FLOAT, INTERNAL_CODEC).xmap(either -> either.map(BlendArgument::new, blend -> blend), blend -> blend.alpha() == 1.0f ? Either.left(Float.valueOf(blend.value())) : Either.right(blend));

    public BlendArgument(float value) {
        this(value, 1.0f);
    }
}

