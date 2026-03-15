package net.minecraft.world.attribute;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Interpolator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeModifier;

public interface ColorModifier<Argument>
extends EnvironmentAttributeModifier<Integer, Argument> {
    public static final ColorModifier<Integer> ALPHA_BLEND = new ColorModifier<Integer>(){

        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return ColorHelper.alphaBlend(integer, integer2);
        }

        @Override
        public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> arg) {
            return Codecs.HEX_ARGB;
        }

        @Override
        public Interpolator<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> arg) {
            return Interpolator.ofColor();
        }

        @Override
        public /* synthetic */ Object apply(Object object, Object object2) {
            return this.apply((Integer)object, (Integer)object2);
        }
    };
    public static final ColorModifier<Integer> ADD = ColorHelper::add;
    public static final ColorModifier<Integer> SUBTRACT = ColorHelper::subtract;
    public static final ColorModifier<Integer> MULTIPLY_RGB = ColorHelper::mix;
    public static final ColorModifier<Integer> MULTIPLY_ARGB = ColorHelper::mix;
    public static final ColorModifier<BlendToGrayArg> BLEND_TO_GRAY = new ColorModifier<BlendToGrayArg>(){

        @Override
        public Integer apply(Integer integer, BlendToGrayArg arg) {
            int i = ColorHelper.scaleRgb(ColorHelper.grayscale(integer), arg.brightness);
            return ColorHelper.lerp(arg.factor, integer, i);
        }

        @Override
        public Codec<BlendToGrayArg> argumentCodec(EnvironmentAttribute<Integer> arg) {
            return BlendToGrayArg.CODEC;
        }

        @Override
        public Interpolator<BlendToGrayArg> argumentKeyframeLerp(EnvironmentAttribute<Integer> arg) {
            return (t, a, b) -> new BlendToGrayArg(MathHelper.lerp(t, a.brightness, b.brightness), MathHelper.lerp(t, a.factor, b.factor));
        }

        @Override
        public /* synthetic */ Object apply(Object object, Object object2) {
            return this.apply((Integer)object, (BlendToGrayArg)object2);
        }
    };

    @FunctionalInterface
    public static interface Rgb
    extends ColorModifier<Integer> {
        @Override
        default public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> arg) {
            return Codecs.HEX_RGB;
        }

        @Override
        default public Interpolator<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> arg) {
            return Interpolator.ofColor();
        }
    }

    @FunctionalInterface
    public static interface Argb
    extends ColorModifier<Integer> {
        @Override
        default public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> arg) {
            return Codec.either(Codecs.HEX_ARGB, Codecs.RGB).xmap(Either::unwrap, argb -> ColorHelper.getAlpha(argb) == 255 ? Either.right(argb) : Either.left(argb));
        }

        @Override
        default public Interpolator<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> arg) {
            return Interpolator.ofColor();
        }
    }

    public record BlendToGrayArg(float brightness, float factor) {
        public static final Codec<BlendToGrayArg> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("brightness")).forGetter(BlendToGrayArg::brightness), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("factor")).forGetter(BlendToGrayArg::factor)).apply((Applicative<BlendToGrayArg, ?>)instance, BlendToGrayArg::new));
    }
}

