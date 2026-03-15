/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/EquipmentModelData;mapToEntityModel(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Ljava/util/function/Function;)Lnet/minecraft/client/render/entity/model/EquipmentModelData;
 *   Lnet/minecraft/client/render/entity/BipedEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/AbstractSkeletonEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/AbstractSkeletonEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;F)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractSkeletonEntityRenderer<T extends AbstractSkeletonEntity, S extends SkeletonEntityRenderState>
extends BipedEntityRenderer<T, S, SkeletonEntityModel<S>> {
    public AbstractSkeletonEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer layer, EquipmentModelData<EntityModelLayer> model) {
        this(context, model, new SkeletonEntityModel(context.getPart(layer)));
    }

    public AbstractSkeletonEntityRenderer(EntityRendererFactory.Context context, EquipmentModelData<EntityModelLayer> arg2, SkeletonEntityModel<S> model) {
        super(context, model, 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, EquipmentModelData.mapToEntityModel(arg2, context.getEntityModels(), SkeletonEntityModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public void updateRenderState(T arg, S arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ((SkeletonEntityRenderState)arg2).attacking = ((MobEntity)arg).isAttacking();
        ((SkeletonEntityRenderState)arg2).shaking = ((AbstractSkeletonEntity)arg).isShaking();
        ((SkeletonEntityRenderState)arg2).holdingBow = ((LivingEntity)arg).getMainHandStack().isOf(Items.BOW);
    }

    @Override
    protected boolean isShaking(S arg) {
        return ((SkeletonEntityRenderState)arg).shaking;
    }

    @Override
    protected BipedEntityModel.ArmPose getArmPose(T arg, Arm arg2) {
        if (((MobEntity)arg).getMainArm() == arg2 && ((MobEntity)arg).isAttacking() && ((LivingEntity)arg).getMainHandStack().isOf(Items.BOW)) {
            return BipedEntityModel.ArmPose.BOW_AND_ARROW;
        }
        return super.getArmPose(arg, arg2);
    }
}

