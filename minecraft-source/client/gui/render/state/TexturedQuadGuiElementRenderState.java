/*
 * External method calls:
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lorg/joml/Matrix3x2fc;FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/gui/ScreenRect;transformEachVertex(Lorg/joml/Matrix3x2fc;)Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/ScreenRect;intersection(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/state/TexturedQuadGuiElementRenderState;createBounds(IIIILorg/joml/Matrix3x2f;Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/TexturedQuadGuiElementRenderState;pose()Lorg/joml/Matrix3x2f;
 */
package net.minecraft.client.gui.render.state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record TexturedQuadGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int x1, int y1, int x2, int y2, float u1, float u2, float v1, float v2, int color, @Nullable ScreenRect scissorArea, @Nullable ScreenRect bounds) implements SimpleGuiElementRenderState
{
    public TexturedQuadGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int x1, int y1, int x2, int y2, float u1, float u2, float v1, float v2, int color, @Nullable ScreenRect scissorArea) {
        this(pipeline, textureSetup, pose, x1, y1, x2, y2, u1, u2, v1, v2, color, scissorArea, TexturedQuadGuiElementRenderState.createBounds(x1, y1, x2, y2, pose, scissorArea));
    }

    @Override
    public void setupVertices(VertexConsumer vertices) {
        vertices.vertex(this.pose(), (float)this.x1(), (float)this.y1()).texture(this.u1(), this.v1()).color(this.color());
        vertices.vertex(this.pose(), (float)this.x1(), (float)this.y2()).texture(this.u1(), this.v2()).color(this.color());
        vertices.vertex(this.pose(), (float)this.x2(), (float)this.y2()).texture(this.u2(), this.v2()).color(this.color());
        vertices.vertex(this.pose(), (float)this.x2(), (float)this.y1()).texture(this.u2(), this.v1()).color(this.color());
    }

    private static @Nullable ScreenRect createBounds(int x1, int y1, int x2, int y2, Matrix3x2f pose, @Nullable ScreenRect scissorArea) {
        ScreenRect lv = new ScreenRect(x1, y1, x2 - x1, y2 - y1).transformEachVertex(pose);
        return scissorArea != null ? scissorArea.intersection(lv) : lv;
    }
}

