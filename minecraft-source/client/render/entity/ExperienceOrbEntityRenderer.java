/*
 * External method calls:
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitCustom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue$Custom;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;overlay(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/RenderLayers;itemEntityTranslucentCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ExperienceOrbEntityRenderer;updateRenderState(Lnet/minecraft/entity/ExperienceOrbEntity;Lnet/minecraft/client/render/entity/state/ExperienceOrbEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/ExperienceOrbEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ExperienceOrbEntityRenderState;
 *   Lnet/minecraft/client/render/entity/ExperienceOrbEntityRenderer;render(Lnet/minecraft/client/render/entity/state/ExperienceOrbEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/ExperienceOrbEntityRenderer;vertex(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/util/math/MatrixStack$Entry;FFIIIFFI)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ExperienceOrbEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class ExperienceOrbEntityRenderer
extends EntityRenderer<ExperienceOrbEntity, ExperienceOrbEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/experience_orb.png");
    private static final RenderLayer LAYER = RenderLayers.itemEntityTranslucentCull(TEXTURE);

    public ExperienceOrbEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.shadowRadius = 0.15f;
        this.shadowOpacity = 0.75f;
    }

    @Override
    protected int getBlockLight(ExperienceOrbEntity arg, BlockPos arg2) {
        return MathHelper.clamp(super.getBlockLight(arg, arg2) + 7, 0, 15);
    }

    @Override
    public void render(ExperienceOrbEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        arg2.push();
        int i = arg.size;
        float f = (float)(i % 4 * 16 + 0) / 64.0f;
        float g = (float)(i % 4 * 16 + 16) / 64.0f;
        float h = (float)(i / 4 * 16 + 0) / 64.0f;
        float j = (float)(i / 4 * 16 + 16) / 64.0f;
        float k = 1.0f;
        float l = 0.5f;
        float m = 0.25f;
        float n = 255.0f;
        float o = arg.age / 2.0f;
        int p = (int)((MathHelper.sin(o + 0.0f) + 1.0f) * 0.5f * 255.0f);
        int q = 255;
        int r = (int)((MathHelper.sin(o + 4.1887903f) + 1.0f) * 0.1f * 255.0f);
        arg2.translate(0.0f, 0.1f, 0.0f);
        arg2.multiply(arg4.orientation);
        float s = 0.3f;
        arg2.scale(0.3f, 0.3f, 0.3f);
        arg3.submitCustom(arg2, LAYER, (matricesEntry, vertexConsumer) -> {
            ExperienceOrbEntityRenderer.vertex(vertexConsumer, matricesEntry, -0.5f, -0.25f, p, 255, r, f, j, arg.light);
            ExperienceOrbEntityRenderer.vertex(vertexConsumer, matricesEntry, 0.5f, -0.25f, p, 255, r, g, j, arg.light);
            ExperienceOrbEntityRenderer.vertex(vertexConsumer, matricesEntry, 0.5f, 0.75f, p, 255, r, g, h, arg.light);
            ExperienceOrbEntityRenderer.vertex(vertexConsumer, matricesEntry, -0.5f, 0.75f, p, 255, r, f, h, arg.light);
        });
        arg2.pop();
        super.render(arg, arg2, arg3, arg4);
    }

    private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
        vertexConsumer.vertex(matrix, x, y, 0.0f).color(red, green, blue, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public ExperienceOrbEntityRenderState createRenderState() {
        return new ExperienceOrbEntityRenderState();
    }

    @Override
    public void updateRenderState(ExperienceOrbEntity arg, ExperienceOrbEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.size = arg.getOrbSize();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

