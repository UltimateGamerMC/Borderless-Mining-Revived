/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/SnifferEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/SnifferEntity;Lnet/minecraft/client/render/entity/state/SnifferEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/SnifferEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/SnifferEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SnifferEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.SnifferEntityRenderState;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

@Environment(value=EnvType.CLIENT)
public class SnifferEntityRenderer
extends AgeableMobEntityRenderer<SnifferEntity, SnifferEntityRenderState, SnifferEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/sniffer/sniffer.png");

    public SnifferEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new SnifferEntityModel(arg.getPart(EntityModelLayers.SNIFFER)), new SnifferEntityModel(arg.getPart(EntityModelLayers.SNIFFER_BABY)), 1.1f);
    }

    @Override
    public Identifier getTexture(SnifferEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public SnifferEntityRenderState createRenderState() {
        return new SnifferEntityRenderState();
    }

    @Override
    public void updateRenderState(SnifferEntity arg, SnifferEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.searching = arg.isSearching();
        arg2.diggingAnimationState.copyFrom(arg.diggingAnimationState);
        arg2.sniffingAnimationState.copyFrom(arg.sniffingAnimationState);
        arg2.risingAnimationState.copyFrom(arg.risingAnimationState);
        arg2.feelingHappyAnimationState.copyFrom(arg.feelingHappyAnimationState);
        arg2.scentingAnimationState.copyFrom(arg.scentingAnimationState);
    }

    @Override
    protected Box getBoundingBox(SnifferEntity arg) {
        return super.getBoundingBox(arg).expand(0.6f);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((SnifferEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

