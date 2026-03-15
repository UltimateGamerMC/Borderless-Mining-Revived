/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/SnowGolemEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/SnowGolemEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/SnowGolemEntity;Lnet/minecraft/client/render/entity/state/SnowGolemEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/SnowGolemEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/SnowGolemEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SnowGolemPumpkinFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.SnowGolemEntityRenderState;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class SnowGolemEntityRenderer
extends MobEntityRenderer<SnowGolemEntity, SnowGolemEntityRenderState, SnowGolemEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/snow_golem.png");

    public SnowGolemEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new SnowGolemEntityModel(arg.getPart(EntityModelLayers.SNOW_GOLEM)), 0.5f);
        this.addFeature(new SnowGolemPumpkinFeatureRenderer(this, arg.getBlockRenderManager()));
    }

    @Override
    public Identifier getTexture(SnowGolemEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public SnowGolemEntityRenderState createRenderState() {
        return new SnowGolemEntityRenderState();
    }

    @Override
    public void updateRenderState(SnowGolemEntity arg, SnowGolemEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.hasPumpkin = arg.hasPumpkin();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((SnowGolemEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

