/*
 * External method calls:
 *   Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;pose()Lorg/joml/Matrix3x2fc;
 *   Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;withOpacity(F)Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;
 *   Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;withPose(Lorg/joml/Matrix3x2fc;)Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;
 *   Lnet/minecraft/client/font/DrawnTextConsumer;text(Lnet/minecraft/client/font/Alignment;IILnet/minecraft/client/font/DrawnTextConsumer$Transformation;Lnet/minecraft/text/Text;)V
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3x2f;

@Environment(value=EnvType.CLIENT)
public class SplashTextRenderer {
    public static final SplashTextRenderer MERRY_X_MAS = new SplashTextRenderer(SplashTextResourceSupplier.MERRY_X_MAS_);
    public static final SplashTextRenderer HAPPY_NEW_YEAR = new SplashTextRenderer(SplashTextResourceSupplier.HAPPY_NEW_YEAR_);
    public static final SplashTextRenderer OOOOO_O_O_OOOOO__SPOOKY = new SplashTextRenderer(SplashTextResourceSupplier.OOOOO_O_O_OOOOO__SPOOKY_);
    private static final int TEXT_X = 123;
    private static final int TEXT_Y = 69;
    private static final float TEXT_ROTATION = -0.34906584f;
    private final Text text;

    public SplashTextRenderer(Text text) {
        this.text = text;
    }

    public void render(DrawContext context, int screenWidth, TextRenderer textRenderer, float alpha) {
        int j = textRenderer.getWidth(this.text);
        DrawnTextConsumer lv = context.getTextConsumer();
        float g = 1.8f - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0f * ((float)Math.PI * 2)) * 0.1f);
        float h = g * 100.0f / (float)(j + 32);
        Matrix3x2f matrix3x2f = new Matrix3x2f(lv.getTransformation().pose()).translate((float)screenWidth / 2.0f + 123.0f, 69.0f).rotate(-0.34906584f).scale(h);
        DrawnTextConsumer.Transformation lv2 = lv.getTransformation().withOpacity(alpha).withPose(matrix3x2f);
        lv.text(Alignment.LEFT, -j / 2, -8, lv2, this.text);
    }
}

