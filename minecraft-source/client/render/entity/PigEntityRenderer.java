/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/PigVariant;modelAndTexture()Lnet/minecraft/util/ModelAndTexture;
 *   Lnet/minecraft/util/ModelAndTexture;model()Ljava/lang/Object;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/util/ModelAndTexture;asset()Lnet/minecraft/util/AssetInfo$TextureAssetInfo;
 *   Lnet/minecraft/util/AssetInfo$TextureAssetInfo;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/PigEntityRenderer;createModelPairs(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/entity/PigEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/PigEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/PigEntity;Lnet/minecraft/client/render/entity/state/PigEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/PigEntityRenderer;render(Lnet/minecraft/client/render/entity/state/PigEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/PigEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/PigEntityRenderState;
 */
package net.minecraft.client.render.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BabyModelPair;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.ColdPigEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PigEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PigVariant;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PigEntityRenderer
extends MobEntityRenderer<PigEntity, PigEntityRenderState, PigEntityModel> {
    private final Map<PigVariant.Model, BabyModelPair<PigEntityModel>> modelPairs;

    public PigEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new PigEntityModel(arg.getPart(EntityModelLayers.PIG)), 0.7f);
        this.modelPairs = PigEntityRenderer.createModelPairs(arg);
        this.addFeature(new SaddleFeatureRenderer<PigEntityRenderState, PigEntityModel, PigEntityModel>(this, arg.getEquipmentRenderer(), EquipmentModel.LayerType.PIG_SADDLE, state -> state.saddleStack, new PigEntityModel(arg.getPart(EntityModelLayers.PIG_SADDLE)), new PigEntityModel(arg.getPart(EntityModelLayers.PIG_BABY_SADDLE))));
    }

    private static Map<PigVariant.Model, BabyModelPair<PigEntityModel>> createModelPairs(EntityRendererFactory.Context context) {
        return Maps.newEnumMap(Map.of(PigVariant.Model.NORMAL, new BabyModelPair<PigEntityModel>(new PigEntityModel(context.getPart(EntityModelLayers.PIG)), new PigEntityModel(context.getPart(EntityModelLayers.PIG_BABY))), PigVariant.Model.COLD, new BabyModelPair<ColdPigEntityModel>(new ColdPigEntityModel(context.getPart(EntityModelLayers.COLD_PIG)), new ColdPigEntityModel(context.getPart(EntityModelLayers.COLD_PIG_BABY)))));
    }

    @Override
    public void render(PigEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.variant == null) {
            return;
        }
        this.model = this.modelPairs.get(arg.variant.modelAndTexture().model()).get(arg.baby);
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public Identifier getTexture(PigEntityRenderState arg) {
        return arg.variant == null ? MissingSprite.getMissingSpriteId() : arg.variant.modelAndTexture().asset().texturePath();
    }

    @Override
    public PigEntityRenderState createRenderState() {
        return new PigEntityRenderState();
    }

    @Override
    public void updateRenderState(PigEntity arg, PigEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.saddleStack = arg.getEquippedStack(EquipmentSlot.SADDLE).copy();
        arg2.variant = arg.getVariant().value();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((PigEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

