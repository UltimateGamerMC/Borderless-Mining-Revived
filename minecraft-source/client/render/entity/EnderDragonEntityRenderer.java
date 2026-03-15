/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;entityAlpha(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/RenderLayers;dragonRays()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;dragonRaysDepth()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitCustom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue$Custom;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *   Lnet/minecraft/world/gen/feature/EndPortalFeature;offsetOrigin(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/boss/dragon/EnderDragonFrameTracker;copyFrom(Lnet/minecraft/entity/boss/dragon/EnderDragonFrameTracker;)V
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;overlay(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lorg/joml/Vector3f;)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;entityDecal(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;eyes(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;entitySmoothCutout(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/EnderDragonEntityRenderer;renderDeathAnimation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/RenderLayer;)V
 *   Lnet/minecraft/client/render/entity/EnderDragonEntityRenderer;renderCrystalBeam(FFFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V
 *   Lnet/minecraft/client/render/entity/EnderDragonEntityRenderer;updateRenderState(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;Lnet/minecraft/client/render/entity/state/EnderDragonEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/EnderDragonEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/EnderDragonEntityRenderState;
 *   Lnet/minecraft/client/render/entity/EnderDragonEntityRenderer;render(Lnet/minecraft/client/render/entity/state/EnderDragonEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.DragonEntityModel;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.EnderDragonEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class EnderDragonEntityRenderer
extends EntityRenderer<EnderDragonEntity, EnderDragonEntityRenderState> {
    public static final Identifier CRYSTAL_BEAM_TEXTURE = Identifier.ofVanilla("textures/entity/end_crystal/end_crystal_beam.png");
    private static final Identifier EXPLOSION_TEXTURE = Identifier.ofVanilla("textures/entity/enderdragon/dragon_exploding.png");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/enderdragon/dragon.png");
    private static final Identifier EYE_TEXTURE = Identifier.ofVanilla("textures/entity/enderdragon/dragon_eyes.png");
    private static final RenderLayer DRAGON_CUTOUT = RenderLayers.entityCutoutNoCull(TEXTURE);
    private static final RenderLayer DRAGON_DECAL = RenderLayers.entityDecal(TEXTURE);
    private static final RenderLayer DRAGON_EYES = RenderLayers.eyes(EYE_TEXTURE);
    private static final RenderLayer CRYSTAL_BEAM_LAYER = RenderLayers.entitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    private final DragonEntityModel model;

    public EnderDragonEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.shadowRadius = 0.5f;
        this.model = new DragonEntityModel(arg.getPart(EntityModelLayers.ENDER_DRAGON));
    }

    @Override
    public void render(EnderDragonEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        arg2.push();
        float f = arg.getLerpedFrame(7).yRot();
        float g = (float)(arg.getLerpedFrame(5).y() - arg.getLerpedFrame(10).y());
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        arg2.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * 10.0f));
        arg2.translate(0.0f, 0.0f, 1.0f);
        arg2.scale(-1.0f, -1.0f, 1.0f);
        arg2.translate(0.0f, -1.501f, 0.0f);
        int i = OverlayTexture.getUv(0.0f, arg.hurt);
        if (arg.ticksSinceDeath > 0.0f) {
            int j = ColorHelper.getWhite(arg.ticksSinceDeath / 200.0f);
            arg3.getBatchingQueue(0).submitModel(this.model, arg, arg2, RenderLayers.entityAlpha(EXPLOSION_TEXTURE), arg.light, OverlayTexture.DEFAULT_UV, j, null, arg.outlineColor, null);
            arg3.getBatchingQueue(1).submitModel(this.model, arg, arg2, DRAGON_DECAL, arg.light, i, -1, null, arg.outlineColor, null);
        } else {
            arg3.getBatchingQueue(0).submitModel(this.model, arg, arg2, DRAGON_CUTOUT, arg.light, i, -1, null, arg.outlineColor, null);
        }
        arg3.submitModel(this.model, arg, arg2, DRAGON_EYES, arg.light, OverlayTexture.DEFAULT_UV, arg.outlineColor, null);
        if (arg.ticksSinceDeath > 0.0f) {
            float h = arg.ticksSinceDeath / 200.0f;
            arg2.push();
            arg2.translate(0.0f, -1.0f, -2.0f);
            EnderDragonEntityRenderer.renderDeathAnimation(arg2, h, arg3, RenderLayers.dragonRays());
            EnderDragonEntityRenderer.renderDeathAnimation(arg2, h, arg3, RenderLayers.dragonRaysDepth());
            arg2.pop();
        }
        arg2.pop();
        if (arg.crystalBeamPos != null) {
            EnderDragonEntityRenderer.renderCrystalBeam((float)arg.crystalBeamPos.x, (float)arg.crystalBeamPos.y, (float)arg.crystalBeamPos.z, arg.age, arg2, arg3, arg.light);
        }
        super.render(arg, arg2, arg3, arg4);
    }

    private static void renderDeathAnimation(MatrixStack matrices, float animationProgress, OrderedRenderCommandQueue queue, RenderLayer renderLayer) {
        queue.submitCustom(matrices, renderLayer, (matricesEntry, vertexConsumer) -> {
            float g = Math.min(animationProgress > 0.8f ? (animationProgress - 0.8f) / 0.2f : 0.0f, 1.0f);
            int i = ColorHelper.fromFloats(1.0f - g, 1.0f, 1.0f, 1.0f);
            int j = 0xFF00FF;
            Random lv = Random.create(432L);
            Vector3f vector3f = new Vector3f();
            Vector3f vector3f2 = new Vector3f();
            Vector3f vector3f3 = new Vector3f();
            Vector3f vector3f4 = new Vector3f();
            Quaternionf quaternionf = new Quaternionf();
            int k = MathHelper.floor((animationProgress + animationProgress * animationProgress) / 2.0f * 60.0f);
            for (int l = 0; l < k; ++l) {
                quaternionf.rotationXYZ(lv.nextFloat() * ((float)Math.PI * 2), lv.nextFloat() * ((float)Math.PI * 2), lv.nextFloat() * ((float)Math.PI * 2)).rotateXYZ(lv.nextFloat() * ((float)Math.PI * 2), lv.nextFloat() * ((float)Math.PI * 2), lv.nextFloat() * ((float)Math.PI * 2) + animationProgress * 1.5707964f);
                matricesEntry.rotate(quaternionf);
                float h = lv.nextFloat() * 20.0f + 5.0f + g * 10.0f;
                float m = lv.nextFloat() * 2.0f + 1.0f + g * 2.0f;
                vector3f2.set(-HALF_SQRT_3 * m, h, -0.5f * m);
                vector3f3.set(HALF_SQRT_3 * m, h, -0.5f * m);
                vector3f4.set(0.0f, h, m);
                vertexConsumer.vertex(matricesEntry, vector3f).color(i);
                vertexConsumer.vertex(matricesEntry, vector3f2).color(0xFF00FF);
                vertexConsumer.vertex(matricesEntry, vector3f3).color(0xFF00FF);
                vertexConsumer.vertex(matricesEntry, vector3f).color(i);
                vertexConsumer.vertex(matricesEntry, vector3f3).color(0xFF00FF);
                vertexConsumer.vertex(matricesEntry, vector3f4).color(0xFF00FF);
                vertexConsumer.vertex(matricesEntry, vector3f).color(i);
                vertexConsumer.vertex(matricesEntry, vector3f4).color(0xFF00FF);
                vertexConsumer.vertex(matricesEntry, vector3f2).color(0xFF00FF);
            }
        });
    }

    public static void renderCrystalBeam(float dx, float dy, float dz, float tickProgress, MatrixStack matrices, OrderedRenderCommandQueue queue, int light) {
        float k = MathHelper.sqrt(dx * dx + dz * dz);
        float l = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        matrices.push();
        matrices.translate(0.0f, 2.0f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2(dz, dx)) - 1.5707964f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)(-Math.atan2(k, dy)) - 1.5707964f));
        float m = 0.0f - tickProgress * 0.01f;
        float n = l / 32.0f - tickProgress * 0.01f;
        queue.submitCustom(matrices, CRYSTAL_BEAM_LAYER, (matricesEntry, vertexConsumer) -> {
            int j = 8;
            float k = 0.0f;
            float l = 0.75f;
            float m = 0.0f;
            for (int n = 1; n <= 8; ++n) {
                float o = MathHelper.sin((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
                float p = MathHelper.cos((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
                float q = (float)n / 8.0f;
                vertexConsumer.vertex(matricesEntry, k * 0.2f, l * 0.2f, 0.0f).color(Colors.BLACK).texture(m, m).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matricesEntry, 0.0f, -1.0f, 0.0f);
                vertexConsumer.vertex(matricesEntry, k, l, l).color(Colors.WHITE).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matricesEntry, 0.0f, -1.0f, 0.0f);
                vertexConsumer.vertex(matricesEntry, o, p, l).color(Colors.WHITE).texture(q, n).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matricesEntry, 0.0f, -1.0f, 0.0f);
                vertexConsumer.vertex(matricesEntry, o * 0.2f, p * 0.2f, 0.0f).color(Colors.BLACK).texture(q, m).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matricesEntry, 0.0f, -1.0f, 0.0f);
                k = o;
                l = p;
                m = q;
            }
        });
        matrices.pop();
    }

    @Override
    public EnderDragonEntityRenderState createRenderState() {
        return new EnderDragonEntityRenderState();
    }

    @Override
    public void updateRenderState(EnderDragonEntity arg, EnderDragonEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.wingPosition = MathHelper.lerp(f, arg.lastWingPosition, arg.wingPosition);
        arg2.ticksSinceDeath = arg.ticksSinceDeath > 0 ? (float)arg.ticksSinceDeath + f : 0.0f;
        arg2.hurt = arg.hurtTime > 0;
        EndCrystalEntity lv = arg.connectedCrystal;
        if (lv != null) {
            Vec3d lv2 = lv.getLerpedPos(f).add(0.0, EndCrystalEntityRenderer.getYOffset((float)lv.endCrystalAge + f), 0.0);
            arg2.crystalBeamPos = lv2.subtract(arg.getLerpedPos(f));
        } else {
            arg2.crystalBeamPos = null;
        }
        Phase lv3 = arg.getPhaseManager().getCurrent();
        arg2.inLandingOrTakeoffPhase = lv3 == PhaseType.LANDING || lv3 == PhaseType.TAKEOFF;
        arg2.sittingOrHovering = lv3.isSittingOrHovering();
        BlockPos lv4 = arg.getEntityWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.offsetOrigin(arg.getFightOrigin()));
        arg2.squaredDistanceFromOrigin = lv4.getSquaredDistance(arg.getEntityPos());
        arg2.tickProgress = arg.isDead() ? 0.0f : f;
        arg2.frameTracker.copyFrom(arg.frameTracker);
    }

    @Override
    protected boolean canBeCulled(EnderDragonEntity arg) {
        return false;
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }

    @Override
    protected /* synthetic */ boolean canBeCulled(Entity entity) {
        return this.canBeCulled((EnderDragonEntity)entity);
    }
}

