/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/FeatureRenderer;renderModel(Lnet/minecraft/client/model/Model;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;II)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public abstract class FeatureRenderer<S extends EntityRenderState, M extends EntityModel<? super S>> {
    private final FeatureRendererContext<S, M> context;

    public FeatureRenderer(FeatureRendererContext<S, M> context) {
        this.context = context;
    }

    protected static <S extends LivingEntityRenderState> void render(Model<? super S> model, Identifier texture, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, S state, int color, int queueOrder) {
        if (!state.invisible) {
            FeatureRenderer.renderModel(model, texture, matrices, queue, light, state, color, queueOrder);
        }
    }

    protected static <S extends LivingEntityRenderState> void renderModel(Model<? super S> model, Identifier texture, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, S state, int color, int queueOrder) {
        queue.getBatchingQueue(queueOrder).submitModel(model, state, matrices, RenderLayers.entityCutoutNoCull(texture), light, LivingEntityRenderer.getOverlay(state, 0.0f), color, null, state.outlineColor, null);
    }

    public M getContextModel() {
        return this.context.getModel();
    }

    public abstract void render(MatrixStack var1, OrderedRenderCommandQueue var2, int var3, S var4, float var5, float var6);
}

