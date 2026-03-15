/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/BipedEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;F)V
 *   Lnet/minecraft/component/type/SwingAnimationComponent;type()Lnet/minecraft/util/SwingAnimationType;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ZombieBaseEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/ZombieBaseEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/ZombieEntity;Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;F)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SwingAnimationComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.SwingAnimationType;

@Environment(value=EnvType.CLIENT)
public abstract class ZombieBaseEntityRenderer<T extends ZombieEntity, S extends ZombieEntityRenderState, M extends ZombieEntityModel<S>>
extends BipedEntityRenderer<T, S, M> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/zombie/zombie.png");

    protected ZombieBaseEntityRenderer(EntityRendererFactory.Context context, M mainModel, M babyMainModel, EquipmentModelData<M> adultModel, EquipmentModelData<M> babyModel) {
        super(context, mainModel, babyMainModel, 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, adultModel, babyModel, context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTexture(S arg) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(T arg, S arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ((ZombieEntityRenderState)arg2).attacking = ((MobEntity)arg).isAttacking();
        ((ZombieEntityRenderState)arg2).convertingInWater = ((ZombieEntity)arg).isConvertingInWater();
    }

    @Override
    protected boolean isShaking(S arg) {
        return super.isShaking(arg) || ((ZombieEntityRenderState)arg).convertingInWater;
    }

    @Override
    protected BipedEntityModel.ArmPose getArmPose(T arg, Arm arg2) {
        SwingAnimationComponent lv = ((LivingEntity)arg).getStackInArm(arg2.getOpposite()).get(DataComponentTypes.SWING_ANIMATION);
        if (lv != null && lv.type() == SwingAnimationType.STAB) {
            return BipedEntityModel.ArmPose.SPEAR;
        }
        return super.getArmPose(arg, arg2);
    }

    @Override
    protected /* synthetic */ boolean isShaking(LivingEntityRenderState state) {
        return this.isShaking((S)((ZombieEntityRenderState)state));
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((S)((ZombieEntityRenderState)state));
    }
}

