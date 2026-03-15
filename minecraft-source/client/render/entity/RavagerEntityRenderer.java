/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/RavagerEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/RavagerEntity;Lnet/minecraft/client/render/entity/state/RavagerEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/RavagerEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/RavagerEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.RavagerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.RavagerEntityRenderState;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class RavagerEntityRenderer
extends MobEntityRenderer<RavagerEntity, RavagerEntityRenderState, RavagerEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/illager/ravager.png");

    public RavagerEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new RavagerEntityModel(arg.getPart(EntityModelLayers.RAVAGER)), 1.1f);
    }

    @Override
    public Identifier getTexture(RavagerEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public RavagerEntityRenderState createRenderState() {
        return new RavagerEntityRenderState();
    }

    @Override
    public void updateRenderState(RavagerEntity arg, RavagerEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.stunTick = (float)arg.getStunTick() > 0.0f ? (float)arg.getStunTick() - f : 0.0f;
        arg2.attackTick = (float)arg.getAttackTick() > 0.0f ? (float)arg.getAttackTick() - f : 0.0f;
        arg2.roarTick = arg.getRoarTick() > 0 ? ((float)(20 - arg.getRoarTick()) + f) / 20.0f : 0.0f;
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((RavagerEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

