/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/WardenEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/WardenEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/WardenEntity;Lnet/minecraft/client/render/entity/state/WardenEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/WardenEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/WardenEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EmissiveFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WardenEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.WardenEntityRenderState;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class WardenEntityRenderer
extends MobEntityRenderer<WardenEntity, WardenEntityRenderState, WardenEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/warden/warden.png");
    private static final Identifier BIOLUMINESCENT_LAYER_TEXTURE = Identifier.ofVanilla("textures/entity/warden/warden_bioluminescent_layer.png");
    private static final Identifier HEART_TEXTURE = Identifier.ofVanilla("textures/entity/warden/warden_heart.png");
    private static final Identifier PULSATING_SPOTS_1_TEXTURE = Identifier.ofVanilla("textures/entity/warden/warden_pulsating_spots_1.png");
    private static final Identifier PULSATING_SPOTS_2_TEXTURE = Identifier.ofVanilla("textures/entity/warden/warden_pulsating_spots_2.png");

    public WardenEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new WardenEntityModel(arg.getPart(EntityModelLayers.WARDEN)), 0.9f);
        WardenEntityModel lv = new WardenEntityModel(arg.getPart(EntityModelLayers.WARDEN_BIOLUMINESCENT));
        WardenEntityModel lv2 = new WardenEntityModel(arg.getPart(EntityModelLayers.WARDEN_PULSATING_SPOTS));
        WardenEntityModel lv3 = new WardenEntityModel(arg.getPart(EntityModelLayers.WARDEN_TENDRILS));
        WardenEntityModel lv4 = new WardenEntityModel(arg.getPart(EntityModelLayers.WARDEN_HEART));
        this.addFeature(new EmissiveFeatureRenderer<WardenEntityRenderState, WardenEntityModel>(this, state -> BIOLUMINESCENT_LAYER_TEXTURE, (state, tickProgress) -> 1.0f, lv, RenderLayers::entityTranslucentEmissive, false));
        this.addFeature(new EmissiveFeatureRenderer<WardenEntityRenderState, WardenEntityModel>(this, state -> PULSATING_SPOTS_1_TEXTURE, (state, tickProgress) -> Math.max(0.0f, MathHelper.cos(tickProgress * 0.045f) * 0.25f), lv2, RenderLayers::entityTranslucentEmissive, false));
        this.addFeature(new EmissiveFeatureRenderer<WardenEntityRenderState, WardenEntityModel>(this, state -> PULSATING_SPOTS_2_TEXTURE, (state, tickProgress) -> Math.max(0.0f, MathHelper.cos(tickProgress * 0.045f + (float)Math.PI) * 0.25f), lv2, RenderLayers::entityTranslucentEmissive, false));
        this.addFeature(new EmissiveFeatureRenderer<WardenEntityRenderState, WardenEntityModel>(this, state -> TEXTURE, (state, tickProgress) -> state.tendrilAlpha, lv3, RenderLayers::entityTranslucentEmissive, false));
        this.addFeature(new EmissiveFeatureRenderer<WardenEntityRenderState, WardenEntityModel>(this, state -> HEART_TEXTURE, (state, tickProgress) -> state.heartAlpha, lv4, RenderLayers::entityTranslucentEmissive, false));
    }

    @Override
    public Identifier getTexture(WardenEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public WardenEntityRenderState createRenderState() {
        return new WardenEntityRenderState();
    }

    @Override
    public void updateRenderState(WardenEntity arg, WardenEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.tendrilAlpha = arg.getTendrilAlpha(f);
        arg2.heartAlpha = arg.getHeartAlpha(f);
        arg2.roaringAnimationState.copyFrom(arg.roaringAnimationState);
        arg2.sniffingAnimationState.copyFrom(arg.sniffingAnimationState);
        arg2.emergingAnimationState.copyFrom(arg.emergingAnimationState);
        arg2.diggingAnimationState.copyFrom(arg.diggingAnimationState);
        arg2.attackingAnimationState.copyFrom(arg.attackingAnimationState);
        arg2.chargingSonicBoomAnimationState.copyFrom(arg.chargingSonicBoomAnimationState);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((WardenEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

