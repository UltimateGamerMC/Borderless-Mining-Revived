/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/EquipmentModelData;mapToEntityModel(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Ljava/util/function/Function;)Lnet/minecraft/client/render/entity/model/EquipmentModelData;
 *   Lnet/minecraft/client/render/entity/BipedEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/PiglinEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/PiglinEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/AbstractPiglinEntity;Lnet/minecraft/client/render/entity/state/PiglinEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/PiglinEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/PiglinEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PiglinEntityRenderState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PiglinEntityRenderer
extends BipedEntityRenderer<AbstractPiglinEntity, PiglinEntityRenderState, PiglinEntityModel> {
    private static final Identifier PIGLIN_TEXTURE = Identifier.ofVanilla("textures/entity/piglin/piglin.png");
    private static final Identifier PIGLIN_BRUTE_TEXTURE = Identifier.ofVanilla("textures/entity/piglin/piglin_brute.png");
    public static final HeadFeatureRenderer.HeadTransformation HEAD_TRANSFORMATION = new HeadFeatureRenderer.HeadTransformation(0.0f, 0.0f, 1.0019531f);

    public PiglinEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer mainLayer, EntityModelLayer babyMainLayer, EquipmentModelData<EntityModelLayer> adultModel, EquipmentModelData<EntityModelLayer> babyModel) {
        super(ctx, new PiglinEntityModel(ctx.getPart(mainLayer)), new PiglinEntityModel(ctx.getPart(babyMainLayer)), 0.5f, HEAD_TRANSFORMATION);
        this.addFeature(new ArmorFeatureRenderer<PiglinEntityRenderState, PiglinEntityModel, PiglinEntityModel>(this, EquipmentModelData.mapToEntityModel(adultModel, ctx.getEntityModels(), PiglinEntityModel::new), EquipmentModelData.mapToEntityModel(babyModel, ctx.getEntityModels(), PiglinEntityModel::new), ctx.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTexture(PiglinEntityRenderState arg) {
        return arg.brute ? PIGLIN_BRUTE_TEXTURE : PIGLIN_TEXTURE;
    }

    @Override
    public PiglinEntityRenderState createRenderState() {
        return new PiglinEntityRenderState();
    }

    @Override
    public void updateRenderState(AbstractPiglinEntity arg, PiglinEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.brute = arg.getType() == EntityType.PIGLIN_BRUTE;
        arg2.activity = arg.getActivity();
        arg2.piglinCrossbowPullTime = CrossbowItem.getPullTime(arg.getActiveItem(), arg);
        arg2.shouldZombify = arg.shouldZombify();
    }

    @Override
    protected boolean isShaking(PiglinEntityRenderState arg) {
        return super.isShaking(arg) || arg.shouldZombify;
    }

    @Override
    protected /* synthetic */ boolean isShaking(LivingEntityRenderState state) {
        return this.isShaking((PiglinEntityRenderState)state);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((PiglinEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

