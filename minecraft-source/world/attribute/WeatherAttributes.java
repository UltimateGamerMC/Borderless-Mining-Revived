/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;timeBased(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/attribute/EnvironmentAttributeFunction$TimeBased;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;apply(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeType;stateChangeLerp()Lnet/minecraft/util/math/Interpolator;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;builder()Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;with(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;Ljava/lang/Object;)Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;with(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/lang/Object;)Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;build()Lnet/minecraft/world/attribute/EnvironmentAttributeMap;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;keySet()Ljava/util/Set;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/attribute/WeatherAttributes;addWeatherAttribute(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;Lnet/minecraft/world/attribute/WeatherAttributes$WeatherAccess;Lnet/minecraft/world/attribute/EnvironmentAttribute;)V
 */
package net.minecraft.world.attribute;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.minecraft.world.attribute.BlendArgument;
import net.minecraft.world.attribute.ColorModifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.FloatModifier;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import net.minecraft.world.attribute.timeline.Timelines;

public class WeatherAttributes {
    public static final EnvironmentAttributeMap RAIN_EFFECTS = EnvironmentAttributeMap.builder().with(EnvironmentAttributes.SKY_COLOR_VISUAL, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGrayArg(0.6f, 0.75f)).with(EnvironmentAttributes.FOG_COLOR_VISUAL, ColorModifier.MULTIPLY_RGB, ColorHelper.fromFloats(1.0f, 0.5f, 0.5f, 0.6f)).with(EnvironmentAttributes.CLOUD_COLOR_VISUAL, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGrayArg(0.24f, 0.5f)).with(EnvironmentAttributes.SKY_LIGHT_LEVEL_GAMEPLAY, FloatModifier.ALPHA_BLEND, new BlendArgument(4.0f, 0.3125f)).with(EnvironmentAttributes.SKY_LIGHT_COLOR_VISUAL, ColorModifier.ALPHA_BLEND, ColorHelper.withAlpha(0.3125f, Timelines.NIGHT_SKY_LIGHT_COLOR)).with(EnvironmentAttributes.SKY_LIGHT_FACTOR_VISUAL, FloatModifier.ALPHA_BLEND, new BlendArgument(0.24f, 0.3125f)).with(EnvironmentAttributes.STAR_BRIGHTNESS_VISUAL, Float.valueOf(0.0f)).with(EnvironmentAttributes.SUNRISE_SUNSET_COLOR_VISUAL, ColorModifier.MULTIPLY_ARGB, ColorHelper.fromFloats(1.0f, 0.5f, 0.5f, 0.6f)).with(EnvironmentAttributes.BEES_STAY_IN_HIVE_GAMEPLAY, true).build();
    public static final EnvironmentAttributeMap THUNDER_EFFECTS = EnvironmentAttributeMap.builder().with(EnvironmentAttributes.SKY_COLOR_VISUAL, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGrayArg(0.24f, 0.94f)).with(EnvironmentAttributes.FOG_COLOR_VISUAL, ColorModifier.MULTIPLY_RGB, ColorHelper.fromFloats(1.0f, 0.25f, 0.25f, 0.3f)).with(EnvironmentAttributes.CLOUD_COLOR_VISUAL, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGrayArg(0.095f, 0.94f)).with(EnvironmentAttributes.SKY_LIGHT_LEVEL_GAMEPLAY, FloatModifier.ALPHA_BLEND, new BlendArgument(4.0f, 0.52734375f)).with(EnvironmentAttributes.SKY_LIGHT_COLOR_VISUAL, ColorModifier.ALPHA_BLEND, ColorHelper.withAlpha(0.52734375f, Timelines.NIGHT_SKY_LIGHT_COLOR)).with(EnvironmentAttributes.SKY_LIGHT_FACTOR_VISUAL, FloatModifier.ALPHA_BLEND, new BlendArgument(0.24f, 0.52734375f)).with(EnvironmentAttributes.STAR_BRIGHTNESS_VISUAL, Float.valueOf(0.0f)).with(EnvironmentAttributes.SUNRISE_SUNSET_COLOR_VISUAL, ColorModifier.MULTIPLY_ARGB, ColorHelper.fromFloats(1.0f, 0.25f, 0.25f, 0.3f)).with(EnvironmentAttributes.BEES_STAY_IN_HIVE_GAMEPLAY, true).build();
    private static final Set<EnvironmentAttribute<?>> ATTRIBUTES = Sets.union(RAIN_EFFECTS.keySet(), THUNDER_EFFECTS.keySet());

    public static void addWeatherAttributes(WorldEnvironmentAttributeAccess.Builder builder, WeatherAccess weather) {
        for (EnvironmentAttribute<?> lv : ATTRIBUTES) {
            WeatherAttributes.addWeatherAttribute(builder, weather, lv);
        }
    }

    private static <Value> void addWeatherAttribute(WorldEnvironmentAttributeAccess.Builder builder, WeatherAccess weather, EnvironmentAttribute<Value> attribute) {
        EnvironmentAttributeMap.Entry lv = RAIN_EFFECTS.getEntry(attribute);
        EnvironmentAttributeMap.Entry lv2 = THUNDER_EFFECTS.getEntry(attribute);
        builder.timeBased(attribute, (value, time) -> {
            Object object2;
            float f = weather.getThunderGradient();
            float g = weather.getRainGradient() - f;
            if (lv != null && g > 0.0f) {
                object2 = lv.apply(value);
                value = attribute.getType().stateChangeLerp().apply(g, value, object2);
            }
            if (lv2 != null && f > 0.0f) {
                object2 = lv2.apply(value);
                value = attribute.getType().stateChangeLerp().apply(f, value, object2);
            }
            return value;
        });
    }

    public static interface WeatherAccess {
        public static WeatherAccess ofWorld(final World world) {
            return new WeatherAccess(){

                @Override
                public float getRainGradient() {
                    return world.getRainGradient(1.0f);
                }

                @Override
                public float getThunderGradient() {
                    return world.getThunderGradient(1.0f);
                }
            };
        }

        public float getRainGradient();

        public float getThunderGradient();
    }
}

