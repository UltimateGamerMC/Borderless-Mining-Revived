/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPart;applyTransform(Lnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/RenderLayers;outlineNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlockStateModel(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/model/BlockStateModel;FFFIII)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/SnowGolemPumpkinFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/SnowGolemEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.client.render.entity.state.SnowGolemEntityRenderState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class SnowGolemPumpkinFeatureRenderer
extends FeatureRenderer<SnowGolemEntityRenderState, SnowGolemEntityModel> {
    private final BlockRenderManager blockRenderManager;

    public SnowGolemPumpkinFeatureRenderer(FeatureRendererContext<SnowGolemEntityRenderState, SnowGolemEntityModel> context, BlockRenderManager blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, SnowGolemEntityRenderState arg3, float f, float g) {
        if (!arg3.hasPumpkin) {
            return;
        }
        if (arg3.invisible && !arg3.hasOutline()) {
            return;
        }
        arg.push();
        ((SnowGolemEntityModel)this.getContextModel()).getHead().applyTransform(arg);
        float h = 0.625f;
        arg.translate(0.0f, -0.34375f, 0.0f);
        arg.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        arg.scale(0.625f, -0.625f, -0.625f);
        BlockState lv = Blocks.CARVED_PUMPKIN.getDefaultState();
        BlockStateModel lv2 = this.blockRenderManager.getModel(lv);
        int j = LivingEntityRenderer.getOverlay(arg3, 0.0f);
        arg.translate(-0.5f, -0.5f, -0.5f);
        RenderLayer lv3 = arg3.hasOutline() && arg3.invisible ? RenderLayers.outlineNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE) : BlockRenderLayers.getEntityBlockLayer(lv);
        arg2.submitBlockStateModel(arg, lv3, lv2, 0.0f, 0.0f, 0.0f, i, j, arg3.outlineColor);
        arg.pop();
    }
}

