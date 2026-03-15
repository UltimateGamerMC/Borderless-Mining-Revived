/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/HappyGhastRopesFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/HappyGhastEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HappyGhastEntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.HappyGhastEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class HappyGhastRopesFeatureRenderer<M extends HappyGhastEntityModel>
extends FeatureRenderer<HappyGhastEntityRenderState, M> {
    private final RenderLayer renderLayer;
    private final HappyGhastEntityModel model;
    private final HappyGhastEntityModel babyModel;

    public HappyGhastRopesFeatureRenderer(FeatureRendererContext<HappyGhastEntityRenderState, M> context, LoadedEntityModels loader, Identifier texture) {
        super(context);
        this.renderLayer = RenderLayers.entityCutoutNoCull(texture);
        this.model = new HappyGhastEntityModel(loader.getModelPart(EntityModelLayers.HAPPY_GHAST_ROPES));
        this.babyModel = new HappyGhastEntityModel(loader.getModelPart(EntityModelLayers.HAPPY_GHAST_BABY_ROPES));
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, HappyGhastEntityRenderState arg3, float f, float g) {
        if (!arg3.hasRopes || !arg3.harnessStack.isIn(ItemTags.HARNESSES)) {
            return;
        }
        HappyGhastEntityModel lv = arg3.baby ? this.babyModel : this.model;
        arg2.submitModel(lv, arg3, arg, this.renderLayer, i, OverlayTexture.DEFAULT_UV, arg3.outlineColor, null);
    }
}

