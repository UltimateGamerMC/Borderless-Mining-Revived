/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/AbstractHoglinEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/HoglinEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/AbstractHoglinEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/HoglinEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.HoglinEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.HoglinEntityRenderState;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.MobEntity;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractHoglinEntityRenderer<T extends MobEntity>
extends AgeableMobEntityRenderer<T, HoglinEntityRenderState, HoglinEntityModel> {
    public AbstractHoglinEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer layer, EntityModelLayer babyLayer, float scale) {
        super(context, new HoglinEntityModel(context.getPart(layer)), new HoglinEntityModel(context.getPart(babyLayer)), scale);
    }

    @Override
    public HoglinEntityRenderState createRenderState() {
        return new HoglinEntityRenderState();
    }

    @Override
    public void updateRenderState(T arg, HoglinEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.movementCooldownTicks = ((Hoglin)arg).getMovementCooldownTicks();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

