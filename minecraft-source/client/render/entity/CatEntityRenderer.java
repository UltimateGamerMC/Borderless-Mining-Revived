/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/passive/CatVariant;assetInfo()Lnet/minecraft/util/AssetInfo$TextureAssetInfo;
 *   Lnet/minecraft/util/AssetInfo$TextureAssetInfo;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/CatEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/CatEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/CatEntity;Lnet/minecraft/client/render/entity/state/CatEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/CatEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/CatEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/client/render/entity/CatEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/CatEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.CatCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class CatEntityRenderer
extends AgeableMobEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel> {
    public CatEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new CatEntityModel(arg.getPart(EntityModelLayers.CAT)), new CatEntityModel(arg.getPart(EntityModelLayers.CAT_BABY)), 0.4f);
        this.addFeature(new CatCollarFeatureRenderer(this, arg.getEntityModels()));
    }

    @Override
    public Identifier getTexture(CatEntityRenderState arg) {
        return arg.texture;
    }

    @Override
    public CatEntityRenderState createRenderState() {
        return new CatEntityRenderState();
    }

    @Override
    public void updateRenderState(CatEntity arg, CatEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.texture = arg.getVariant().value().assetInfo().texturePath();
        arg2.inSneakingPose = arg.isInSneakingPose();
        arg2.sprinting = arg.isSprinting();
        arg2.inSittingPose = arg.isInSittingPose();
        arg2.sleepAnimationProgress = arg.getSleepAnimationProgress(f);
        arg2.tailCurlAnimationProgress = arg.getTailCurlAnimationProgress(f);
        arg2.headDownAnimationProgress = arg.getHeadDownAnimationProgress(f);
        arg2.nearSleepingPlayer = arg.isNearSleepingPlayer();
        arg2.collarColor = arg.isTamed() ? arg.getCollarColor() : null;
    }

    @Override
    protected void setupTransforms(CatEntityRenderState arg, MatrixStack arg2, float f, float g) {
        super.setupTransforms(arg, arg2, f, g);
        float h = arg.sleepAnimationProgress;
        if (h > 0.0f) {
            arg2.translate(0.4f * h, 0.15f * h, 0.1f * h);
            arg2.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerpAngleDegrees(h, 0.0f, 90.0f)));
            if (arg.nearSleepingPlayer) {
                arg2.translate(0.15f * h, 0.0f, 0.0f);
            }
        }
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((CatEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

