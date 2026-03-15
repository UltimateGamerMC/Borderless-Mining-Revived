/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/BreezeEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/BreezeEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/BreezeEntity;Lnet/minecraft/client/render/entity/state/BreezeEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/BreezeEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/BreezeEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.BreezeEyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.BreezeWindFeatureRenderer;
import net.minecraft.client.render.entity.model.BreezeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BreezeEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BreezeEntityRenderer
extends MobEntityRenderer<BreezeEntity, BreezeEntityRenderState, BreezeEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/breeze/breeze.png");

    public BreezeEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new BreezeEntityModel(arg.getPart(EntityModelLayers.BREEZE)), 0.5f);
        this.addFeature(new BreezeWindFeatureRenderer(this, arg.getEntityModels()));
        this.addFeature(new BreezeEyesFeatureRenderer(this, arg.getEntityModels()));
    }

    @Override
    public Identifier getTexture(BreezeEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public BreezeEntityRenderState createRenderState() {
        return new BreezeEntityRenderState();
    }

    @Override
    public void updateRenderState(BreezeEntity arg, BreezeEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.idleAnimationState.copyFrom(arg.idleAnimationState);
        arg2.shootingAnimationState.copyFrom(arg.shootingAnimationState);
        arg2.slidingAnimationState.copyFrom(arg.slidingAnimationState);
        arg2.slidingBackAnimationState.copyFrom(arg.slidingBackAnimationState);
        arg2.inhalingAnimationState.copyFrom(arg.inhalingAnimationState);
        arg2.longJumpingAnimationState.copyFrom(arg.longJumpingAnimationState);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((BreezeEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

