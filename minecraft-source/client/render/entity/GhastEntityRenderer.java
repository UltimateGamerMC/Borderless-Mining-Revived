/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/GhastEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/GhastEntity;Lnet/minecraft/client/render/entity/state/GhastEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/GhastEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/GhastEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.GhastEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class GhastEntityRenderer
extends MobEntityRenderer<GhastEntity, GhastEntityRenderState, GhastEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/ghast/ghast.png");
    private static final Identifier SHOOTING_TEXTURE = Identifier.ofVanilla("textures/entity/ghast/ghast_shooting.png");

    public GhastEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new GhastEntityModel(arg.getPart(EntityModelLayers.GHAST)), 1.5f);
    }

    @Override
    public Identifier getTexture(GhastEntityRenderState arg) {
        if (arg.shooting) {
            return SHOOTING_TEXTURE;
        }
        return TEXTURE;
    }

    @Override
    public GhastEntityRenderState createRenderState() {
        return new GhastEntityRenderState();
    }

    @Override
    public void updateRenderState(GhastEntity arg, GhastEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.shooting = arg.isShooting();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((GhastEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

