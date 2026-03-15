/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;update(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/FoxEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/FoxEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/FoxEntity;Lnet/minecraft/client/render/entity/state/FoxEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/FoxEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/FoxEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/client/render/entity/FoxEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/FoxEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FoxHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.FoxEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.FoxEntityRenderState;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class FoxEntityRenderer
extends AgeableMobEntityRenderer<FoxEntity, FoxEntityRenderState, FoxEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/fox/fox.png");
    private static final Identifier SLEEPING_TEXTURE = Identifier.ofVanilla("textures/entity/fox/fox_sleep.png");
    private static final Identifier SNOW_TEXTURE = Identifier.ofVanilla("textures/entity/fox/snow_fox.png");
    private static final Identifier SLEEPING_SNOW_TEXTURE = Identifier.ofVanilla("textures/entity/fox/snow_fox_sleep.png");

    public FoxEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new FoxEntityModel(arg.getPart(EntityModelLayers.FOX)), new FoxEntityModel(arg.getPart(EntityModelLayers.FOX_BABY)), 0.4f);
        this.addFeature(new FoxHeldItemFeatureRenderer(this));
    }

    @Override
    protected void setupTransforms(FoxEntityRenderState arg, MatrixStack arg2, float f, float g) {
        super.setupTransforms(arg, arg2, f, g);
        if (arg.chasing || arg.walking) {
            arg2.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-arg.pitch));
        }
    }

    @Override
    public Identifier getTexture(FoxEntityRenderState arg) {
        if (arg.type == FoxEntity.Variant.RED) {
            return arg.sleeping ? SLEEPING_TEXTURE : TEXTURE;
        }
        return arg.sleeping ? SLEEPING_SNOW_TEXTURE : SNOW_TEXTURE;
    }

    @Override
    public FoxEntityRenderState createRenderState() {
        return new FoxEntityRenderState();
    }

    @Override
    public void updateRenderState(FoxEntity arg, FoxEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ItemHolderEntityRenderState.update(arg, arg2, this.itemModelResolver);
        arg2.headRoll = arg.getHeadRoll(f);
        arg2.inSneakingPose = arg.isInSneakingPose();
        arg2.bodyRotationHeightOffset = arg.getBodyRotationHeightOffset(f);
        arg2.sleeping = arg.isSleeping();
        arg2.sitting = arg.isSitting();
        arg2.walking = arg.isWalking();
        arg2.chasing = arg.isChasing();
        arg2.type = arg.getVariant();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((FoxEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

