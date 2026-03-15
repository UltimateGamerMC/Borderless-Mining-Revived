/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/SheepEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/SheepEntityRenderer;nameEquals(Lnet/minecraft/entity/Entity;Ljava/lang/String;)Z
 *   Lnet/minecraft/client/render/entity/SheepEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/SheepEntity;Lnet/minecraft/client/render/entity/state/SheepEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/SheepEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/SheepEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolUndercoatFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class SheepEntityRenderer
extends AgeableMobEntityRenderer<SheepEntity, SheepEntityRenderState, SheepEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/sheep/sheep.png");

    public SheepEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new SheepEntityModel(arg.getPart(EntityModelLayers.SHEEP)), new SheepEntityModel(arg.getPart(EntityModelLayers.SHEEP_BABY)), 0.7f);
        this.addFeature(new SheepWoolUndercoatFeatureRenderer(this, arg.getEntityModels()));
        this.addFeature(new SheepWoolFeatureRenderer(this, arg.getEntityModels()));
    }

    @Override
    public Identifier getTexture(SheepEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public SheepEntityRenderState createRenderState() {
        return new SheepEntityRenderState();
    }

    @Override
    public void updateRenderState(SheepEntity arg, SheepEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.headAngle = arg.getHeadAngle(f);
        arg2.neckAngle = arg.getNeckAngle(f);
        arg2.sheared = arg.isSheared();
        arg2.color = arg.getColor();
        arg2.rainbow = SheepEntityRenderer.nameEquals(arg, "jeb_");
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((SheepEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

