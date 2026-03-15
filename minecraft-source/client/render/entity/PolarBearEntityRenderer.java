/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/PolarBearEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/PolarBearEntity;Lnet/minecraft/client/render/entity/state/PolarBearEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/PolarBearEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/PolarBearEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PolarBearEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PolarBearEntityRenderState;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PolarBearEntityRenderer
extends AgeableMobEntityRenderer<PolarBearEntity, PolarBearEntityRenderState, PolarBearEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/bear/polarbear.png");

    public PolarBearEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new PolarBearEntityModel(arg.getPart(EntityModelLayers.POLAR_BEAR)), new PolarBearEntityModel(arg.getPart(EntityModelLayers.POLAR_BEAR_BABY)), 0.9f);
    }

    @Override
    public Identifier getTexture(PolarBearEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public PolarBearEntityRenderState createRenderState() {
        return new PolarBearEntityRenderState();
    }

    @Override
    public void updateRenderState(PolarBearEntity arg, PolarBearEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.warningAnimationProgress = arg.getWarningAnimationProgress(f);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((PolarBearEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

