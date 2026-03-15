/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/CamelEntityRenderer;createSaddleFeatureRenderer(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Lnet/minecraft/client/render/entity/feature/SaddleFeatureRenderer;
 *   Lnet/minecraft/client/render/entity/CamelEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/CamelEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/CamelEntity;Lnet/minecraft/client/render/entity/state/CamelEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/CamelEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/CamelEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.CamelEntityModel;
import net.minecraft.client.render.entity.model.CamelSaddleEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.CamelEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class CamelEntityRenderer
extends AgeableMobEntityRenderer<CamelEntity, CamelEntityRenderState, CamelEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/camel/camel.png");

    public CamelEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new CamelEntityModel(arg.getPart(EntityModelLayers.CAMEL)), new CamelEntityModel(arg.getPart(EntityModelLayers.CAMEL_BABY)), 0.7f);
        this.addFeature(this.createSaddleFeatureRenderer(arg));
    }

    protected SaddleFeatureRenderer<CamelEntityRenderState, CamelEntityModel, CamelSaddleEntityModel> createSaddleFeatureRenderer(EntityRendererFactory.Context context) {
        return new SaddleFeatureRenderer<CamelEntityRenderState, CamelEntityModel, CamelSaddleEntityModel>(this, context.getEquipmentRenderer(), EquipmentModel.LayerType.CAMEL_SADDLE, state -> state.saddleStack, new CamelSaddleEntityModel(context.getPart(EntityModelLayers.CAMEL_SADDLE)), new CamelSaddleEntityModel(context.getPart(EntityModelLayers.CAMEL_BABY_SADDLE)));
    }

    @Override
    public Identifier getTexture(CamelEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public CamelEntityRenderState createRenderState() {
        return new CamelEntityRenderState();
    }

    @Override
    public void updateRenderState(CamelEntity arg, CamelEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.saddleStack = arg.getEquippedStack(EquipmentSlot.SADDLE).copy();
        arg2.hasPassengers = arg.hasPassengers();
        arg2.jumpCooldown = Math.max((float)arg.getJumpCooldown() - f, 0.0f);
        arg2.sittingTransitionAnimationState.copyFrom(arg.sittingTransitionAnimationState);
        arg2.sittingAnimationState.copyFrom(arg.sittingAnimationState);
        arg2.standingTransitionAnimationState.copyFrom(arg.standingTransitionAnimationState);
        arg2.idlingAnimationState.copyFrom(arg.idlingAnimationState);
        arg2.dashingAnimationState.copyFrom(arg.dashingAnimationState);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((CamelEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

