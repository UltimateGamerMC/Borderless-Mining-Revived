package net.minecraft.enchantment.effect.value;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.util.math.random.Random;

public record RemoveBinomialEnchantmentEffect(EnchantmentLevelBasedValue chance) implements EnchantmentValueEffect
{
    public static final MapCodec<RemoveBinomialEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)EnchantmentLevelBasedValue.CODEC.fieldOf("chance")).forGetter(RemoveBinomialEnchantmentEffect::chance)).apply((Applicative<RemoveBinomialEnchantmentEffect, ?>)instance, RemoveBinomialEnchantmentEffect::new));

    @Override
    public float apply(int level, Random random, float inputValue) {
        float g = this.chance.getValue(level);
        int j = 0;
        if (inputValue <= 128.0f || inputValue * g < 20.0f || inputValue * (1.0f - g) < 20.0f) {
            int k = 0;
            while ((float)k < inputValue) {
                if (random.nextFloat() < g) {
                    ++j;
                }
                ++k;
            }
        } else {
            double d = Math.floor(inputValue * g);
            double e = Math.sqrt(inputValue * g * (1.0f - g));
            j = (int)Math.round(d + random.nextGaussian() * e);
            j = Math.clamp((long)j, 0, (int)inputValue);
        }
        return inputValue - (float)j;
    }

    public MapCodec<RemoveBinomialEnchantmentEffect> getCodec() {
        return CODEC;
    }
}

