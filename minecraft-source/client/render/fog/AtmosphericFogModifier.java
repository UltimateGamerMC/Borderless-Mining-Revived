/*
 * External method calls:
 *   Lnet/minecraft/client/render/CameraOverride;forwardVector()Lorg/joml/Vector3fc;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/fog/AtmosphericFogModifier;method_76556(IFF)I
 *   Lnet/minecraft/client/render/fog/AtmosphericFogModifier;method_76304(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/render/RenderTickCounter;)V
 */
package net.minecraft.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraOverride;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.biome.Biome;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class AtmosphericFogModifier
extends FogModifier {
    private static final int field_60795 = 8;
    private static final float field_60587 = -160.0f;
    private static final float field_60588 = -256.0f;
    private float fogMultiplier;

    @Override
    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        float h;
        int j = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.FOG_COLOR_VISUAL, skyDarkness);
        if (viewDistance >= 4) {
            int l;
            float m;
            float g = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SUN_ANGLE_VISUAL, skyDarkness).floatValue() * ((float)Math.PI / 180);
            h = MathHelper.sin(g) > 0.0f ? -1.0f : 1.0f;
            CameraOverride lv = MinecraftClient.getInstance().gameRenderer.getCameraOverride();
            Vector3fc vector3fc = lv != null ? lv.forwardVector() : camera.getHorizontalPlane();
            float k = vector3fc.dot(h, 0.0f, 0.0f);
            if (k > 0.0f && (m = ColorHelper.getAlphaFloat(l = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SUNRISE_SUNSET_COLOR_VISUAL, skyDarkness).intValue())) > 0.0f) {
                j = ColorHelper.lerp(k * m, j, ColorHelper.fullAlpha(l));
            }
        }
        int n = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_COLOR_VISUAL, skyDarkness);
        n = AtmosphericFogModifier.method_76556(n, world.getRainGradient(skyDarkness), world.getThunderGradient(skyDarkness));
        h = Math.min(camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_FOG_END_DISTANCE_VISUAL, skyDarkness).floatValue() / 16.0f, (float)viewDistance);
        float o = MathHelper.clampedLerp(h / 32.0f, 0.25f, 1.0f);
        o = 1.0f - (float)Math.pow(o, 0.25);
        j = ColorHelper.lerp(o, j, n);
        return j;
    }

    private static int method_76556(int i, float f, float g) {
        if (f > 0.0f) {
            float h = 1.0f - f * 0.5f;
            float j = 1.0f - f * 0.4f;
            i = ColorHelper.scaleRgb(i, h, h, j);
        }
        if (g > 0.0f) {
            i = ColorHelper.scaleRgb(i, 1.0f - g * 0.5f);
        }
        return i;
    }

    @Override
    public void applyStartEndModifier(FogData data, Camera arg2, ClientWorld arg3, float f, RenderTickCounter arg4) {
        this.method_76304(arg2, arg3, arg4);
        float g = arg4.getTickProgress(false);
        data.environmentalStart = arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.FOG_START_DISTANCE_VISUAL, g).floatValue();
        data.environmentalEnd = arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.FOG_END_DISTANCE_VISUAL, g).floatValue();
        data.environmentalStart += -160.0f * this.fogMultiplier;
        float h = Math.min(96.0f, data.environmentalEnd);
        data.environmentalEnd = Math.max(h, data.environmentalEnd + -256.0f * this.fogMultiplier);
        data.skyEnd = Math.min(f, arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_FOG_END_DISTANCE_VISUAL, g).floatValue());
        data.cloudEnd = Math.min((float)(MinecraftClient.getInstance().options.getCloudRenderDistance().getValue() * 16), arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE_VISUAL, g).floatValue());
        if (MinecraftClient.getInstance().inGameHud.getBossBarHud().shouldThickenFog()) {
            data.environmentalStart = Math.min(data.environmentalStart, 10.0f);
            data.skyEnd = data.environmentalEnd = Math.min(data.environmentalEnd, 96.0f);
            data.cloudEnd = data.environmentalEnd;
        }
    }

    private void method_76304(Camera arg, ClientWorld arg2, RenderTickCounter arg3) {
        BlockPos lv = arg.getBlockPos();
        Biome lv2 = arg2.getBiome(lv).value();
        float f = arg3.getDynamicDeltaTicks();
        float g = arg3.getTickProgress(false);
        boolean bl = lv2.hasPrecipitation();
        float h = MathHelper.clamp(((float)arg2.getLightingProvider().get(LightType.SKY).getLightLevel(lv) - 8.0f) / 7.0f, 0.0f, 1.0f);
        float i = arg2.getRainGradient(g) * h * (bl ? 1.0f : 0.5f);
        this.fogMultiplier += (i - this.fogMultiplier) * f * 0.2f;
    }

    @Override
    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        return submersionType == CameraSubmersionType.ATMOSPHERIC;
    }
}

