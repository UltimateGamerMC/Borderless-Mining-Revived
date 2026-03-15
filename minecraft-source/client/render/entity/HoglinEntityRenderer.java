/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AbstractHoglinEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/entity/state/HoglinEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/HoglinEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/HoglinEntity;Lnet/minecraft/client/render/entity/state/HoglinEntityRenderState;F)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHoglinEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.HoglinEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class HoglinEntityRenderer
extends AbstractHoglinEntityRenderer<HoglinEntity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/hoglin/hoglin.png");

    public HoglinEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, EntityModelLayers.HOGLIN, EntityModelLayers.HOGLIN_BABY, 0.7f);
    }

    @Override
    public Identifier getTexture(HoglinEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(HoglinEntity arg, HoglinEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.canConvert = arg.canConvert();
    }

    @Override
    protected boolean isShaking(HoglinEntityRenderState arg) {
        return super.isShaking(arg) || arg.canConvert;
    }

    @Override
    protected /* synthetic */ boolean isShaking(LivingEntityRenderState state) {
        return this.isShaking((HoglinEntityRenderState)state);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((HoglinEntityRenderState)state);
    }
}

