/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/ChickenVariant;modelAndTexture()Lnet/minecraft/util/ModelAndTexture;
 *   Lnet/minecraft/util/ModelAndTexture;model()Ljava/lang/Object;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/util/ModelAndTexture;asset()Lnet/minecraft/util/AssetInfo$TextureAssetInfo;
 *   Lnet/minecraft/util/AssetInfo$TextureAssetInfo;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ChickenEntityRenderer;createBabyModelPairMap(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/entity/ChickenEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/ChickenEntity;Lnet/minecraft/client/render/entity/state/ChickenEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/ChickenEntityRenderer;render(Lnet/minecraft/client/render/entity/state/ChickenEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/ChickenEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ChickenEntityRenderState;
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
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.ColdChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.ChickenEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class ChickenEntityRenderer
extends MobEntityRenderer<ChickenEntity, ChickenEntityRenderState, ChickenEntityModel> {
    private final Map<ChickenVariant.Model, BabyModelPair<ChickenEntityModel>> babyModelPairMap;

    public ChickenEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new ChickenEntityModel(arg.getPart(EntityModelLayers.CHICKEN)), 0.3f);
        this.babyModelPairMap = ChickenEntityRenderer.createBabyModelPairMap(arg);
    }

    private static Map<ChickenVariant.Model, BabyModelPair<ChickenEntityModel>> createBabyModelPairMap(EntityRendererFactory.Context context) {
        return Maps.newEnumMap(Map.of(ChickenVariant.Model.NORMAL, new BabyModelPair<ChickenEntityModel>(new ChickenEntityModel(context.getPart(EntityModelLayers.CHICKEN)), new ChickenEntityModel(context.getPart(EntityModelLayers.CHICKEN_BABY))), ChickenVariant.Model.COLD, new BabyModelPair<ColdChickenEntityModel>(new ColdChickenEntityModel(context.getPart(EntityModelLayers.COLD_CHICKEN)), new ColdChickenEntityModel(context.getPart(EntityModelLayers.COLD_CHICKEN_BABY)))));
    }

    @Override
    public void render(ChickenEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.variant == null) {
            return;
        }
        this.model = this.babyModelPairMap.get(arg.variant.modelAndTexture().model()).get(arg.baby);
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public Identifier getTexture(ChickenEntityRenderState arg) {
        return arg.variant == null ? MissingSprite.getMissingSpriteId() : arg.variant.modelAndTexture().asset().texturePath();
    }

    @Override
    public ChickenEntityRenderState createRenderState() {
        return new ChickenEntityRenderState();
    }

    @Override
    public void updateRenderState(ChickenEntity arg, ChickenEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.flapProgress = MathHelper.lerp(f, arg.lastFlapProgress, arg.flapProgress);
        arg2.maxWingDeviation = MathHelper.lerp(f, arg.lastMaxWingDeviation, arg.maxWingDeviation);
        arg2.variant = arg.getVariant().value();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((ChickenEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

