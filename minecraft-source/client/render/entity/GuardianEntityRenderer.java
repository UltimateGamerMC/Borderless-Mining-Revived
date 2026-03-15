/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitCustom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue$Custom;)V
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;overlay(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;fromLerpedPosition(Lnet/minecraft/entity/LivingEntity;DF)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;renderBeam(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/util/math/Vec3d;FFF)V
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/GuardianEntity;Lnet/minecraft/client/render/entity/state/GuardianEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;render(Lnet/minecraft/client/render/entity/state/GuardianEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/GuardianEntityRenderState;
 *   Lnet/minecraft/client/render/entity/GuardianEntityRenderer;vertex(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/util/math/MatrixStack$Entry;FFFIIIFF)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GuardianEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.GuardianEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class GuardianEntityRenderer
extends MobEntityRenderer<GuardianEntity, GuardianEntityRenderState, GuardianEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/guardian.png");
    private static final Identifier EXPLOSION_BEAM_TEXTURE = Identifier.ofVanilla("textures/entity/guardian_beam.png");
    private static final RenderLayer LAYER = RenderLayers.entityCutoutNoCull(EXPLOSION_BEAM_TEXTURE);

    public GuardianEntityRenderer(EntityRendererFactory.Context arg) {
        this(arg, 0.5f, EntityModelLayers.GUARDIAN);
    }

    protected GuardianEntityRenderer(EntityRendererFactory.Context ctx, float shadowRadius, EntityModelLayer layer) {
        super(ctx, new GuardianEntityModel(ctx.getPart(layer)), shadowRadius);
    }

    @Override
    public boolean shouldRender(GuardianEntity arg, Frustum arg2, double d, double e, double f) {
        LivingEntity lv;
        if (super.shouldRender(arg, arg2, d, e, f)) {
            return true;
        }
        if (arg.hasBeamTarget() && (lv = arg.getBeamTarget()) != null) {
            Vec3d lv2 = this.fromLerpedPosition(lv, (double)lv.getHeight() * 0.5, 1.0f);
            Vec3d lv3 = this.fromLerpedPosition(arg, arg.getStandingEyeHeight(), 1.0f);
            return arg2.isVisible(new Box(lv3.x, lv3.y, lv3.z, lv2.x, lv2.y, lv2.z));
        }
        return false;
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double e = MathHelper.lerp((double)delta, entity.lastRenderX, entity.getX());
        double g = MathHelper.lerp((double)delta, entity.lastRenderY, entity.getY()) + yOffset;
        double h = MathHelper.lerp((double)delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(e, g, h);
    }

    @Override
    public void render(GuardianEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        super.render(arg, arg2, arg3, arg4);
        Vec3d lv = arg.beamTargetPos;
        if (lv != null) {
            float f = arg.beamTicks * 0.5f % 1.0f;
            arg2.push();
            arg2.translate(0.0f, arg.standingEyeHeight, 0.0f);
            GuardianEntityRenderer.renderBeam(arg2, arg3, lv.subtract(arg.cameraPosVec), arg.beamTicks, arg.beamProgress, f);
            arg2.pop();
        }
    }

    private static void renderBeam(MatrixStack matrices, OrderedRenderCommandQueue queue, Vec3d offset, float beamTicks, float beamProgress, float h) {
        float i = (float)(offset.length() + 1.0);
        offset = offset.normalize();
        float j = (float)Math.acos(offset.y);
        float k = 1.5707964f - (float)Math.atan2(offset.z, offset.x);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k * 57.295776f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j * 57.295776f));
        float l = beamTicks * 0.05f * -1.5f;
        float m = beamProgress * beamProgress;
        int n = 64 + (int)(m * 191.0f);
        int o = 32 + (int)(m * 191.0f);
        int p = 128 - (int)(m * 64.0f);
        float q = 0.2f;
        float r = 0.282f;
        float s = MathHelper.cos(l + 2.3561945f) * 0.282f;
        float t = MathHelper.sin(l + 2.3561945f) * 0.282f;
        float u = MathHelper.cos(l + 0.7853982f) * 0.282f;
        float v = MathHelper.sin(l + 0.7853982f) * 0.282f;
        float w = MathHelper.cos(l + 3.926991f) * 0.282f;
        float x = MathHelper.sin(l + 3.926991f) * 0.282f;
        float y = MathHelper.cos(l + 5.4977875f) * 0.282f;
        float z = MathHelper.sin(l + 5.4977875f) * 0.282f;
        float aa = MathHelper.cos(l + (float)Math.PI) * 0.2f;
        float ab = MathHelper.sin(l + (float)Math.PI) * 0.2f;
        float ac = MathHelper.cos(l + 0.0f) * 0.2f;
        float ad = MathHelper.sin(l + 0.0f) * 0.2f;
        float ae = MathHelper.cos(l + 1.5707964f) * 0.2f;
        float af = MathHelper.sin(l + 1.5707964f) * 0.2f;
        float ag = MathHelper.cos(l + 4.712389f) * 0.2f;
        float ah = MathHelper.sin(l + 4.712389f) * 0.2f;
        float ai = i;
        float aj = 0.0f;
        float ak = 0.4999f;
        float al = -1.0f + h;
        float am = al + i * 2.5f;
        queue.submitCustom(matrices, LAYER, (matricesEntry, vertexConsumer) -> {
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, aa, ai, ab, n, o, p, 0.4999f, am);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, aa, 0.0f, ab, n, o, p, 0.4999f, al);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ac, 0.0f, ad, n, o, p, 0.0f, al);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ac, ai, ad, n, o, p, 0.0f, am);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ae, ai, af, n, o, p, 0.4999f, am);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ae, 0.0f, af, n, o, p, 0.4999f, al);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ag, 0.0f, ah, n, o, p, 0.0f, al);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, ag, ai, ah, n, o, p, 0.0f, am);
            float ac = MathHelper.floor(beamTicks) % 2 == 0 ? 0.5f : 0.0f;
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, s, ai, t, n, o, p, 0.5f, ac + 0.5f);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, u, ai, v, n, o, p, 1.0f, ac + 0.5f);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, y, ai, z, n, o, p, 1.0f, ac);
            GuardianEntityRenderer.vertex(vertexConsumer, matricesEntry, w, ai, x, n, o, p, 0.5f, ac);
        });
    }

    private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(matrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(matrix, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public Identifier getTexture(GuardianEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public GuardianEntityRenderState createRenderState() {
        return new GuardianEntityRenderState();
    }

    @Override
    public void updateRenderState(GuardianEntity arg, GuardianEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.spikesExtension = arg.getSpikesExtension(f);
        arg2.tailAngle = arg.getTailAngle(f);
        arg2.cameraPosVec = arg.getCameraPosVec(f);
        Entity lv = GuardianEntityRenderer.getBeamTarget(arg);
        if (lv != null) {
            arg2.rotationVec = arg.getRotationVec(f);
            arg2.lookAtPos = lv.getCameraPosVec(f);
        } else {
            arg2.rotationVec = null;
            arg2.lookAtPos = null;
        }
        LivingEntity lv2 = arg.getBeamTarget();
        if (lv2 != null) {
            arg2.beamProgress = arg.getBeamProgress(f);
            arg2.beamTicks = arg.getBeamTicks() + f;
            arg2.beamTargetPos = this.fromLerpedPosition(lv2, (double)lv2.getHeight() * 0.5, f);
        } else {
            arg2.beamTargetPos = null;
        }
    }

    private static @Nullable Entity getBeamTarget(GuardianEntity guardian) {
        Entity lv = MinecraftClient.getInstance().getCameraEntity();
        if (guardian.hasBeamTarget()) {
            return guardian.getBeamTarget();
        }
        return lv;
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((GuardianEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

