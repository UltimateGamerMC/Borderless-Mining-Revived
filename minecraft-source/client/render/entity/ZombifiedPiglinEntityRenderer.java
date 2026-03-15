/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/EquipmentModelData;mapToEntityModel(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Ljava/util/function/Function;)Lnet/minecraft/client/render/entity/model/EquipmentModelData;
 *   Lnet/minecraft/client/render/entity/BipedEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ZombifiedPiglinEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/ZombifiedPiglinEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;Lnet/minecraft/client/render/entity/state/ZombifiedPiglinEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/ZombifiedPiglinEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ZombifiedPiglinEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PiglinEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.ZombifiedPiglinEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.ZombifiedPiglinEntityRenderState;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ZombifiedPiglinEntityRenderer
extends BipedEntityRenderer<ZombifiedPiglinEntity, ZombifiedPiglinEntityRenderState, ZombifiedPiglinEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/piglin/zombified_piglin.png");

    public ZombifiedPiglinEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer mainLayer, EntityModelLayer babyMainLayer, EquipmentModelData<EntityModelLayer> adultModel, EquipmentModelData<EntityModelLayer> babyModel) {
        super(context, new ZombifiedPiglinEntityModel(context.getPart(mainLayer)), new ZombifiedPiglinEntityModel(context.getPart(babyMainLayer)), 0.5f, PiglinEntityRenderer.HEAD_TRANSFORMATION);
        this.addFeature(new ArmorFeatureRenderer<ZombifiedPiglinEntityRenderState, ZombifiedPiglinEntityModel, ZombifiedPiglinEntityModel>(this, EquipmentModelData.mapToEntityModel(adultModel, context.getEntityModels(), ZombifiedPiglinEntityModel::new), EquipmentModelData.mapToEntityModel(babyModel, context.getEntityModels(), ZombifiedPiglinEntityModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTexture(ZombifiedPiglinEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public ZombifiedPiglinEntityRenderState createRenderState() {
        return new ZombifiedPiglinEntityRenderState();
    }

    @Override
    public void updateRenderState(ZombifiedPiglinEntity arg, ZombifiedPiglinEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.attacking = arg.isAttacking();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((ZombifiedPiglinEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

