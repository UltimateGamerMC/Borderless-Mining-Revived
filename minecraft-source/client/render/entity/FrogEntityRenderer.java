/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/entity/passive/FrogVariant;assetInfo()Lnet/minecraft/util/AssetInfo$TextureAssetInfo;
 *   Lnet/minecraft/util/AssetInfo$TextureAssetInfo;texturePath()Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/FrogEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/FrogEntity;Lnet/minecraft/client/render/entity/state/FrogEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/FrogEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/FrogEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.FrogEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.FrogEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class FrogEntityRenderer
extends MobEntityRenderer<FrogEntity, FrogEntityRenderState, FrogEntityModel> {
    public FrogEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new FrogEntityModel(arg.getPart(EntityModelLayers.FROG)), 0.3f);
    }

    @Override
    public Identifier getTexture(FrogEntityRenderState arg) {
        return arg.texture;
    }

    @Override
    public FrogEntityRenderState createRenderState() {
        return new FrogEntityRenderState();
    }

    @Override
    public void updateRenderState(FrogEntity arg, FrogEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.insideWaterOrBubbleColumn = arg.isTouchingWater();
        arg2.longJumpingAnimationState.copyFrom(arg.longJumpingAnimationState);
        arg2.croakingAnimationState.copyFrom(arg.croakingAnimationState);
        arg2.usingTongueAnimationState.copyFrom(arg.usingTongueAnimationState);
        arg2.idlingInWaterAnimationState.copyFrom(arg.idlingInWaterAnimationState);
        arg2.texture = arg.getVariant().value().assetInfo().texturePath();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((FrogEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

