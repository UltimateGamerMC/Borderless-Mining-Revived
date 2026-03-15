/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AbstractSkeletonEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/BoggedEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/BoggedEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/BoggedEntity;Lnet/minecraft/client/render/entity/state/BoggedEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/BoggedEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/BoggedEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.BoggedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.state.BoggedEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BoggedEntityRenderer
extends AbstractSkeletonEntityRenderer<BoggedEntity, BoggedEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/bogged.png");
    private static final Identifier OVERLAY_TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/bogged_overlay.png");

    public BoggedEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, EntityModelLayers.BOGGED_EQUIPMENT, new BoggedEntityModel(arg.getPart(EntityModelLayers.BOGGED)));
        this.addFeature(new SkeletonOverlayFeatureRenderer<BoggedEntityRenderState, SkeletonEntityModel<BoggedEntityRenderState>>(this, arg.getEntityModels(), EntityModelLayers.BOGGED_OUTER, OVERLAY_TEXTURE));
    }

    @Override
    public Identifier getTexture(BoggedEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public BoggedEntityRenderState createRenderState() {
        return new BoggedEntityRenderState();
    }

    @Override
    public void updateRenderState(BoggedEntity arg, BoggedEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.sheared = arg.isSheared();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((BoggedEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

