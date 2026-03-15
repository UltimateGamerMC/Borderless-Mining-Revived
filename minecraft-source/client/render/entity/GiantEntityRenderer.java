/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/EquipmentModelData;mapToEntityModel(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Ljava/util/function/Function;)Lnet/minecraft/client/render/entity/model/EquipmentModelData;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/BipedEntityRenderer;updateBipedRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;FLnet/minecraft/client/item/ItemModelManager;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/GiantEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/GiantEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/GiantEntity;Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/GiantEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.GiantEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class GiantEntityRenderer
extends MobEntityRenderer<GiantEntity, ZombieEntityRenderState, BipedEntityModel<ZombieEntityRenderState>> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/zombie/zombie.png");

    public GiantEntityRenderer(EntityRendererFactory.Context ctx, float scale) {
        super(ctx, new GiantEntityModel(ctx.getPart(EntityModelLayers.GIANT)), 0.5f * scale);
        this.addFeature(new HeldItemFeatureRenderer<ZombieEntityRenderState, BipedEntityModel<ZombieEntityRenderState>>(this));
        this.addFeature(new ArmorFeatureRenderer<ZombieEntityRenderState, BipedEntityModel<ZombieEntityRenderState>, GiantEntityModel>(this, EquipmentModelData.mapToEntityModel(EntityModelLayers.GIANT_EQUIPMENT, ctx.getEntityModels(), GiantEntityModel::new), ctx.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTexture(ZombieEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public ZombieEntityRenderState createRenderState() {
        return new ZombieEntityRenderState();
    }

    @Override
    public void updateRenderState(GiantEntity arg, ZombieEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        BipedEntityRenderer.updateBipedRenderState(arg, arg2, f, this.itemModelResolver);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((ZombieEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

