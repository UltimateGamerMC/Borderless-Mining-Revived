/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/BatEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/BatEntity;Lnet/minecraft/client/render/entity/state/BatEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/BatEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/BatEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BatEntityRenderer
extends MobEntityRenderer<BatEntity, BatEntityRenderState, BatEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/bat.png");

    public BatEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new BatEntityModel(arg.getPart(EntityModelLayers.BAT)), 0.25f);
    }

    @Override
    public Identifier getTexture(BatEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public BatEntityRenderState createRenderState() {
        return new BatEntityRenderState();
    }

    @Override
    public void updateRenderState(BatEntity arg, BatEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.roosting = arg.isRoosting();
        arg2.flyingAnimationState.copyFrom(arg.flyingAnimationState);
        arg2.roostingAnimationState.copyFrom(arg.roostingAnimationState);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((BatEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

