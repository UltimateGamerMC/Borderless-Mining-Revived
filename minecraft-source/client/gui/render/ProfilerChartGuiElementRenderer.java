/*
 * External method calls:
 *   Lnet/minecraft/client/gui/render/state/special/ProfilerChartGuiElementRenderState;chartData()Ljava/util/List;
 *   Lnet/minecraft/client/render/RenderLayers;debugTriangleFan()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lorg/joml/Matrix4fc;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/RenderLayers;debugQuads()Lnet/minecraft/client/render/RenderLayer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/ProfilerChartGuiElementRenderer;render(Lnet/minecraft/client/gui/render/state/special/ProfilerChartGuiElementRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V
 */
package net.minecraft.client.gui.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.gui.render.state.special.ProfilerChartGuiElementRenderState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.ProfilerTiming;
import org.joml.Matrix4f;

@Environment(value=EnvType.CLIENT)
public class ProfilerChartGuiElementRenderer
extends SpecialGuiElementRenderer<ProfilerChartGuiElementRenderState> {
    public ProfilerChartGuiElementRenderer(VertexConsumerProvider.Immediate arg) {
        super(arg);
    }

    @Override
    public Class<ProfilerChartGuiElementRenderState> getElementClass() {
        return ProfilerChartGuiElementRenderState.class;
    }

    @Override
    protected void render(ProfilerChartGuiElementRenderState arg, MatrixStack arg2) {
        double d = 0.0;
        arg2.translate(0.0f, -5.0f, 0.0f);
        Matrix4f matrix4f = arg2.peek().getPositionMatrix();
        for (ProfilerTiming lv : arg.chartData()) {
            float h;
            float g;
            float f;
            int l;
            int i = MathHelper.floor(lv.parentSectionUsagePercentage / 4.0) + 1;
            VertexConsumer lv2 = this.vertexConsumers.getBuffer(RenderLayers.debugTriangleFan());
            int j = ColorHelper.fullAlpha(lv.getColor());
            int k = ColorHelper.mix(j, Colors.GRAY);
            lv2.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(j);
            for (l = i; l >= 0; --l) {
                f = (float)((d + lv.parentSectionUsagePercentage * (double)l / (double)i) * 6.2831854820251465 / 100.0);
                g = MathHelper.sin(f) * 105.0f;
                h = MathHelper.cos(f) * 105.0f * 0.5f;
                lv2.vertex(matrix4f, g, h, 0.0f).color(j);
            }
            lv2 = this.vertexConsumers.getBuffer(RenderLayers.debugQuads());
            for (l = i; l > 0; --l) {
                f = (float)((d + lv.parentSectionUsagePercentage * (double)l / (double)i) * 6.2831854820251465 / 100.0);
                g = MathHelper.sin(f) * 105.0f;
                h = MathHelper.cos(f) * 105.0f * 0.5f;
                float m = (float)((d + lv.parentSectionUsagePercentage * (double)(l - 1) / (double)i) * 6.2831854820251465 / 100.0);
                float n = MathHelper.sin(m) * 105.0f;
                float o = MathHelper.cos(m) * 105.0f * 0.5f;
                if ((h + o) / 2.0f < 0.0f) continue;
                lv2.vertex(matrix4f, g, h, 0.0f).color(k);
                lv2.vertex(matrix4f, g, h + 10.0f, 0.0f).color(k);
                lv2.vertex(matrix4f, n, o + 10.0f, 0.0f).color(k);
                lv2.vertex(matrix4f, n, o, 0.0f).color(k);
            }
            d += lv.parentSectionUsagePercentage;
        }
    }

    @Override
    protected float getYOffset(int height, int windowScaleFactor) {
        return (float)height / 2.0f;
    }

    @Override
    protected String getName() {
        return "profiler chart";
    }
}

