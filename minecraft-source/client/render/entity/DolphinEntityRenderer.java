/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;update(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/DolphinEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/DolphinEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/DolphinEntity;Lnet/minecraft/client/render/entity/state/DolphinEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/DolphinEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/DolphinEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.DolphinHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.DolphinEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.DolphinEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class DolphinEntityRenderer
extends AgeableMobEntityRenderer<DolphinEntity, DolphinEntityRenderState, DolphinEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/dolphin.png");

    public DolphinEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new DolphinEntityModel(arg.getPart(EntityModelLayers.DOLPHIN)), new DolphinEntityModel(arg.getPart(EntityModelLayers.DOLPHIN_BABY)), 0.7f);
        this.addFeature(new DolphinHeldItemFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(DolphinEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public DolphinEntityRenderState createRenderState() {
        return new DolphinEntityRenderState();
    }

    @Override
    public void updateRenderState(DolphinEntity arg, DolphinEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ItemHolderEntityRenderState.update(arg, arg2, this.itemModelResolver);
        arg2.moving = arg.getVelocity().horizontalLengthSquared() > 1.0E-7;
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((DolphinEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

