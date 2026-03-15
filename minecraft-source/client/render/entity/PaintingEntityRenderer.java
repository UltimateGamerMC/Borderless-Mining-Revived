/*
 * External method calls:
 *   Lnet/minecraft/entity/decoration/painting/PaintingVariant;assetId()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/RenderLayers;entitySolidZOffsetForward(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitCustom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue$Custom;)V
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;overlay(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/PaintingEntityRenderer;renderPainting(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/RenderLayer;[IIILnet/minecraft/client/texture/Sprite;Lnet/minecraft/client/texture/Sprite;)V
 *   Lnet/minecraft/client/render/entity/PaintingEntityRenderer;updateRenderState(Lnet/minecraft/entity/decoration/painting/PaintingEntity;Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/PaintingEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;
 *   Lnet/minecraft/client/render/entity/PaintingEntityRenderer;render(Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/PaintingEntityRenderer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;FFFFFIIII)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PaintingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Atlases;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(value=EnvType.CLIENT)
public class PaintingEntityRenderer
extends EntityRenderer<PaintingEntity, PaintingEntityRenderState> {
    private static final Identifier BACK_TEXTURE = Identifier.ofVanilla("back");
    private final SpriteAtlasTexture paintingAtlases;

    public PaintingEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.paintingAtlases = arg.getSpriteAtlasTexture(Atlases.PAINTINGS);
    }

    @Override
    public void render(PaintingEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        PaintingVariant lv = arg.variant;
        if (lv == null) {
            return;
        }
        arg2.push();
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 - arg.facing.getHorizontalQuarterTurns() * 90));
        Sprite lv2 = this.paintingAtlases.getSprite(lv.assetId());
        Sprite lv3 = this.paintingAtlases.getSprite(BACK_TEXTURE);
        this.renderPainting(arg2, arg3, RenderLayers.entitySolidZOffsetForward(lv3.getAtlasId()), arg.lightmapCoordinates, lv.width(), lv.height(), lv2, lv3);
        arg2.pop();
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public PaintingEntityRenderState createRenderState() {
        return new PaintingEntityRenderState();
    }

    @Override
    public void updateRenderState(PaintingEntity arg, PaintingEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        Direction lv = arg.getHorizontalFacing();
        PaintingVariant lv2 = arg.getVariant().value();
        arg2.facing = lv;
        arg2.variant = lv2;
        int i = lv2.width();
        int j = lv2.height();
        if (arg2.lightmapCoordinates.length != i * j) {
            arg2.lightmapCoordinates = new int[i * j];
        }
        float g = (float)(-i) / 2.0f;
        float h = (float)(-j) / 2.0f;
        World lv3 = arg.getEntityWorld();
        for (int k = 0; k < j; ++k) {
            for (int l = 0; l < i; ++l) {
                float m = (float)l + g + 0.5f;
                float n = (float)k + h + 0.5f;
                int o = arg.getBlockX();
                int p = MathHelper.floor(arg.getY() + (double)n);
                int q = arg.getBlockZ();
                switch (lv) {
                    case NORTH: {
                        o = MathHelper.floor(arg.getX() + (double)m);
                        break;
                    }
                    case WEST: {
                        q = MathHelper.floor(arg.getZ() - (double)m);
                        break;
                    }
                    case SOUTH: {
                        o = MathHelper.floor(arg.getX() - (double)m);
                        break;
                    }
                    case EAST: {
                        q = MathHelper.floor(arg.getZ() + (double)m);
                    }
                }
                arg2.lightmapCoordinates[l + k * i] = WorldRenderer.getLightmapCoordinates(lv3, new BlockPos(o, p, q));
            }
        }
    }

    private void renderPainting(MatrixStack matrices, OrderedRenderCommandQueue queue, RenderLayer renderLayer, int[] lightmapCoordinates, int width, int height, Sprite front, Sprite back) {
        queue.submitCustom(matrices, renderLayer, (matricesEntry, vertexConsumer) -> {
            float f = (float)(-width) / 2.0f;
            float g = (float)(-height) / 2.0f;
            float h = 0.03125f;
            float k = back.getMinU();
            float l = back.getMaxU();
            float m = back.getMinV();
            float n = back.getMaxV();
            float o = back.getMinU();
            float p = back.getMaxU();
            float q = back.getMinV();
            float r = back.getFrameV(0.0625f);
            float s = back.getMinU();
            float t = back.getFrameU(0.0625f);
            float u = back.getMinV();
            float v = back.getMaxV();
            double d = 1.0 / (double)width;
            double e = 1.0 / (double)height;
            for (int w = 0; w < width; ++w) {
                for (int x = 0; x < height; ++x) {
                    float y = f + (float)(w + 1);
                    float z = f + (float)w;
                    float aa = g + (float)(x + 1);
                    float ab = g + (float)x;
                    int ac = lightmapCoordinates[w + x * width];
                    float ad = front.getFrameU((float)(d * (double)(width - w)));
                    float ae = front.getFrameU((float)(d * (double)(width - (w + 1))));
                    float af = front.getFrameV((float)(e * (double)(height - x)));
                    float ag = front.getFrameV((float)(e * (double)(height - (x + 1))));
                    this.vertex(matricesEntry, vertexConsumer, y, ab, ae, af, -0.03125f, 0, 0, -1, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, ad, af, -0.03125f, 0, 0, -1, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, ad, ag, -0.03125f, 0, 0, -1, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, ae, ag, -0.03125f, 0, 0, -1, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, l, m, 0.03125f, 0, 0, 1, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, k, m, 0.03125f, 0, 0, 1, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, k, n, 0.03125f, 0, 0, 1, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, ab, l, n, 0.03125f, 0, 0, 1, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, o, q, -0.03125f, 0, 1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, p, q, -0.03125f, 0, 1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, p, r, 0.03125f, 0, 1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, o, r, 0.03125f, 0, 1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, ab, o, q, 0.03125f, 0, -1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, p, q, 0.03125f, 0, -1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, p, r, -0.03125f, 0, -1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, ab, o, r, -0.03125f, 0, -1, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, t, u, 0.03125f, -1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, ab, t, v, 0.03125f, -1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, ab, s, v, -0.03125f, -1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, y, aa, s, u, -0.03125f, -1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, t, u, -0.03125f, 1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, t, v, -0.03125f, 1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, ab, s, v, 0.03125f, 1, 0, 0, ac);
                    this.vertex(matricesEntry, vertexConsumer, z, aa, s, u, 0.03125f, 1, 0, 0, ac);
                }
            }
        });
    }

    private void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float u, float v, float z, int normalX, int normalY, int normalZ, int light) {
        vertexConsumer.vertex(matrix, x, y, z).color(Colors.WHITE).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix, normalX, normalY, normalZ);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

