/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/MipmapHelper;blendDarkenedCutout(IIII)I
 *   Lnet/minecraft/client/texture/MipmapHelper;adjustAlphaForTargetCoverage(Lnet/minecraft/client/texture/NativeImage;FFF)V
 */
package net.minecraft.client.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

@Environment(value=EnvType.CLIENT)
public class MipmapHelper {
    private static final String ITEM_PREFIX = "item/";
    private static final float DEFAULT_ALPHA_THRESHOLD = 0.5f;
    private static final float STRICT_CUTOUT_ALPHA_THRESHOLD = 0.3f;

    private MipmapHelper() {
    }

    private static float getOpacityCoverage(NativeImage image, float alphaThreshold, float alphaMulti) {
        int i = image.getWidth();
        int j = image.getHeight();
        float h = 0.0f;
        int k = 4;
        for (int l = 0; l < j - 1; ++l) {
            for (int m = 0; m < i - 1; ++m) {
                float n = Math.clamp(ColorHelper.getAlphaFloat(image.getColorArgb(m, l)) * alphaMulti, 0.0f, 1.0f);
                float o = Math.clamp(ColorHelper.getAlphaFloat(image.getColorArgb(m + 1, l)) * alphaMulti, 0.0f, 1.0f);
                float p = Math.clamp(ColorHelper.getAlphaFloat(image.getColorArgb(m, l + 1)) * alphaMulti, 0.0f, 1.0f);
                float q = Math.clamp(ColorHelper.getAlphaFloat(image.getColorArgb(m + 1, l + 1)) * alphaMulti, 0.0f, 1.0f);
                float r = 0.0f;
                for (int s = 0; s < 4; ++s) {
                    float t = ((float)s + 0.5f) / 4.0f;
                    for (int u = 0; u < 4; ++u) {
                        float v = ((float)u + 0.5f) / 4.0f;
                        float w = n * (1.0f - v) * (1.0f - t) + o * v * (1.0f - t) + p * (1.0f - v) * t + q * v * t;
                        if (!(w > alphaThreshold)) continue;
                        r += 1.0f;
                    }
                }
                h += r / 16.0f;
            }
        }
        return h / (float)((i - 1) * (j - 1));
    }

    private static void adjustAlphaForTargetCoverage(NativeImage image, float targetCoverage, float alphaThreshold, float cutoffBias) {
        int p;
        float i = 0.0f;
        float j = 4.0f;
        float k = 1.0f;
        float l = 1.0f;
        float m = Float.MAX_VALUE;
        int n = image.getWidth();
        int o = image.getHeight();
        for (p = 0; p < 5; ++p) {
            float q = MipmapHelper.getOpacityCoverage(image, alphaThreshold, k);
            float r = Math.abs(q - targetCoverage);
            if (r < m) {
                m = r;
                l = k;
            }
            if (q < targetCoverage) {
                i = k;
            } else {
                if (!(q > targetCoverage)) break;
                j = k;
            }
            k = (i + j) * 0.5f;
        }
        for (p = 0; p < o; ++p) {
            for (int s = 0; s < n; ++s) {
                int t = image.getColorArgb(s, p);
                float u = ColorHelper.getAlphaFloat(t);
                u = u * l + cutoffBias + 0.025f;
                u = Math.clamp(u, 0.0f, 1.0f);
                image.setColorArgb(s, p, ColorHelper.withAlpha(u, t));
            }
        }
    }

    public static NativeImage[] getMipmapLevelsImages(Identifier id, NativeImage[] mipmapLevelImages, int mipmapLevels, MipmapStrategy strategy, float cutoffBias) {
        if (strategy == MipmapStrategy.AUTO) {
            MipmapStrategy mipmapStrategy = strategy = MipmapHelper.hasAlpha(mipmapLevelImages[0]) ? MipmapStrategy.CUTOUT : MipmapStrategy.MEAN;
        }
        if (mipmapLevelImages.length == 1 && !id.getPath().startsWith(ITEM_PREFIX)) {
            if (strategy == MipmapStrategy.CUTOUT || strategy == MipmapStrategy.STRICT_CUTOUT) {
                TextureUtil.solidify(mipmapLevelImages[0]);
            } else if (strategy == MipmapStrategy.DARK_CUTOUT) {
                TextureUtil.fillEmptyAreasWithDarkColor(mipmapLevelImages[0]);
            }
        }
        if (mipmapLevels + 1 <= mipmapLevelImages.length) {
            return mipmapLevelImages;
        }
        NativeImage[] lvs = new NativeImage[mipmapLevels + 1];
        lvs[0] = mipmapLevelImages[0];
        boolean bl = strategy == MipmapStrategy.CUTOUT || strategy == MipmapStrategy.STRICT_CUTOUT || strategy == MipmapStrategy.DARK_CUTOUT;
        float g = strategy == MipmapStrategy.STRICT_CUTOUT ? 0.3f : 0.5f;
        float h = bl ? MipmapHelper.getOpacityCoverage(mipmapLevelImages[0], g, 1.0f) : 0.0f;
        for (int j = 1; j <= mipmapLevels; ++j) {
            if (j < mipmapLevelImages.length) {
                lvs[j] = mipmapLevelImages[j];
            } else {
                NativeImage lv = lvs[j - 1];
                NativeImage lv2 = new NativeImage(lv.getWidth() >> 1, lv.getHeight() >> 1, false);
                int k = lv2.getWidth();
                int l = lv2.getHeight();
                for (int m = 0; m < k; ++m) {
                    for (int n = 0; n < l; ++n) {
                        int o = lv.getColorArgb(m * 2 + 0, n * 2 + 0);
                        int p = lv.getColorArgb(m * 2 + 1, n * 2 + 0);
                        int q = lv.getColorArgb(m * 2 + 0, n * 2 + 1);
                        int r = lv.getColorArgb(m * 2 + 1, n * 2 + 1);
                        int s = strategy == MipmapStrategy.DARK_CUTOUT ? MipmapHelper.blendDarkenedCutout(o, p, q, r) : ColorHelper.interpolate(o, p, q, r);
                        lv2.setColorArgb(m, n, s);
                    }
                }
                lvs[j] = lv2;
            }
            if (!bl) continue;
            MipmapHelper.adjustAlphaForTargetCoverage(lvs[j], h, g, cutoffBias);
        }
        return lvs;
    }

    private static boolean hasAlpha(NativeImage image) {
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                if (ColorHelper.getAlpha(image.getColorArgb(i, j)) != 0) continue;
                return true;
            }
        }
        return false;
    }

    private static int blendDarkenedCutout(int nw, int ne, int sw, int se) {
        float f = 0.0f;
        float g = 0.0f;
        float h = 0.0f;
        float m = 0.0f;
        if (ColorHelper.getAlpha(nw) != 0) {
            f += ColorHelper.srgbToLinear(ColorHelper.getAlpha(nw));
            g += ColorHelper.srgbToLinear(ColorHelper.getRed(nw));
            h += ColorHelper.srgbToLinear(ColorHelper.getGreen(nw));
            m += ColorHelper.srgbToLinear(ColorHelper.getBlue(nw));
        }
        if (ColorHelper.getAlpha(ne) != 0) {
            f += ColorHelper.srgbToLinear(ColorHelper.getAlpha(ne));
            g += ColorHelper.srgbToLinear(ColorHelper.getRed(ne));
            h += ColorHelper.srgbToLinear(ColorHelper.getGreen(ne));
            m += ColorHelper.srgbToLinear(ColorHelper.getBlue(ne));
        }
        if (ColorHelper.getAlpha(sw) != 0) {
            f += ColorHelper.srgbToLinear(ColorHelper.getAlpha(sw));
            g += ColorHelper.srgbToLinear(ColorHelper.getRed(sw));
            h += ColorHelper.srgbToLinear(ColorHelper.getGreen(sw));
            m += ColorHelper.srgbToLinear(ColorHelper.getBlue(sw));
        }
        if (ColorHelper.getAlpha(se) != 0) {
            f += ColorHelper.srgbToLinear(ColorHelper.getAlpha(se));
            g += ColorHelper.srgbToLinear(ColorHelper.getRed(se));
            h += ColorHelper.srgbToLinear(ColorHelper.getGreen(se));
            m += ColorHelper.srgbToLinear(ColorHelper.getBlue(se));
        }
        return ColorHelper.getArgb(ColorHelper.linearToSrgb(f /= 4.0f), ColorHelper.linearToSrgb(g /= 4.0f), ColorHelper.linearToSrgb(h /= 4.0f), ColorHelper.linearToSrgb(m /= 4.0f));
    }
}

