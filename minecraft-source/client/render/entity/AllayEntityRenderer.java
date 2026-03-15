/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/AllayEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/AllayEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/AllayEntity;Lnet/minecraft/client/render/entity/state/AllayEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/AllayEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/AllayEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.AllayEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.AllayEntityRenderState;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(value=EnvType.CLIENT)
public class AllayEntityRenderer
extends MobEntityRenderer<AllayEntity, AllayEntityRenderState, AllayEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/allay/allay.png");

    public AllayEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new AllayEntityModel(arg.getPart(EntityModelLayers.ALLAY)), 0.4f);
        this.addFeature(new HeldItemFeatureRenderer<AllayEntityRenderState, AllayEntityModel>(this));
    }

    @Override
    public Identifier getTexture(AllayEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public AllayEntityRenderState createRenderState() {
        return new AllayEntityRenderState();
    }

    @Override
    public void updateRenderState(AllayEntity arg, AllayEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ArmedEntityRenderState.updateRenderState(arg, arg2, this.itemModelResolver, f);
        arg2.dancing = arg.isDancing();
        arg2.spinning = arg.isSpinning();
        arg2.spinningAnimationTicks = arg.getSpinningAnimationTicks(f);
        arg2.itemHoldAnimationTicks = arg.getItemHoldAnimationTicks(f);
    }

    @Override
    protected int getBlockLight(AllayEntity arg, BlockPos arg2) {
        return 15;
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((AllayEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

