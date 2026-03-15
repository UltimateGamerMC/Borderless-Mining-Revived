/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/CopperGolemOxidationLevel;texture()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;F)V
 *   Lnet/minecraft/entity/AnimationState;copyFrom(Lnet/minecraft/entity/AnimationState;)V
 *   Lnet/minecraft/component/type/BlockStateComponent;applyToState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/entity/passive/CopperGolemOxidationLevel;eyeTexture()Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/CopperGolemEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/CopperGolemEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/CopperGolemEntity;Lnet/minecraft/client/render/entity/state/CopperGolemEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/CopperGolemEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/CopperGolemEntityRenderState;
 */
package net.minecraft.client.render.entity;

import java.util.Optional;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.CopperGolemHeadBlockFeatureRenderer;
import net.minecraft.client.render.entity.feature.EmissiveFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.CopperGolemEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.CopperGolemEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.passive.CopperGolemEntity;
import net.minecraft.entity.passive.CopperGolemOxidationLevels;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class CopperGolemEntityRenderer
extends MobEntityRenderer<CopperGolemEntity, CopperGolemEntityRenderState, CopperGolemEntityModel> {
    public CopperGolemEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new CopperGolemEntityModel(arg.getPart(EntityModelLayers.COPPER_GOLEM)), 0.5f);
        this.addFeature(new EmissiveFeatureRenderer<CopperGolemEntityRenderState, CopperGolemEntityModel>(this, CopperGolemEntityRenderer.getEyeTextureGetter(), (state, tickProgress) -> 1.0f, new CopperGolemEntityModel(arg.getPart(EntityModelLayers.COPPER_GOLEM)), RenderLayers::eyes, false));
        this.addFeature(new HeldItemFeatureRenderer<CopperGolemEntityRenderState, CopperGolemEntityModel>(this));
        this.addFeature(new CopperGolemHeadBlockFeatureRenderer<CopperGolemEntityRenderState, CopperGolemEntityModel>(this, state -> state.headBlockItemStack, ((CopperGolemEntityModel)this.model)::transformMatricesForBlock));
        this.addFeature(new HeadFeatureRenderer<CopperGolemEntityRenderState, CopperGolemEntityModel>(this, arg.getEntityModels(), arg.getPlayerSkinCache()));
    }

    @Override
    public Identifier getTexture(CopperGolemEntityRenderState arg) {
        return CopperGolemOxidationLevels.get(arg.oxidationLevel).texture();
    }

    private static Function<CopperGolemEntityRenderState, Identifier> getEyeTextureGetter() {
        return state -> CopperGolemOxidationLevels.get(state.oxidationLevel).eyeTexture();
    }

    @Override
    public CopperGolemEntityRenderState createRenderState() {
        return new CopperGolemEntityRenderState();
    }

    @Override
    public void updateRenderState(CopperGolemEntity arg, CopperGolemEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ArmedEntityRenderState.updateRenderState(arg, arg2, this.itemModelResolver, f);
        arg2.oxidationLevel = arg.getOxidationLevel();
        arg2.copperGolemState = arg.getState();
        arg2.spinHeadAnimationState.copyFrom(arg.getSpinHeadAnimationState());
        arg2.gettingItemAnimationState.copyFrom(arg.getGettingItemAnimationState());
        arg2.gettingNoItemAnimationState.copyFrom(arg.getGettingNoItemAnimationState());
        arg2.droppingItemAnimationState.copyFrom(arg.getDroppingItemAnimationState());
        arg2.droppingNoItemAnimationState.copyFrom(arg.getDroppingNoItemAnimationState());
        arg2.headBlockItemStack = Optional.of(arg.getEquippedStack(CopperGolemEntity.POPPY_SLOT)).flatMap(stack -> {
            Item lv = stack.getItem();
            if (!(lv instanceof BlockItem)) {
                return Optional.empty();
            }
            BlockItem lv2 = (BlockItem)lv;
            BlockStateComponent lv3 = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
            return Optional.of(lv3.applyToState(lv2.getBlock().getDefaultState()));
        });
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((CopperGolemEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

