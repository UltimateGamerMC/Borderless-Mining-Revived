/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPart;applyTransform(Lnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/RenderLayers;outlineNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlockStateModel(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/model/BlockStateModel;FFFIII)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlock(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/block/BlockState;III)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/MooshroomMushroomFeatureRenderer;renderMushroom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IZILnet/minecraft/block/BlockState;ILnet/minecraft/client/render/model/BlockStateModel;)V
 *   Lnet/minecraft/client/render/entity/feature/MooshroomMushroomFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/MooshroomEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.state.MooshroomEntityRenderState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class MooshroomMushroomFeatureRenderer
extends FeatureRenderer<MooshroomEntityRenderState, CowEntityModel> {
    private final BlockRenderManager blockRenderManager;

    public MooshroomMushroomFeatureRenderer(FeatureRendererContext<MooshroomEntityRenderState, CowEntityModel> context, BlockRenderManager blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, MooshroomEntityRenderState arg3, float f, float g) {
        boolean bl;
        if (arg3.baby) {
            return;
        }
        boolean bl2 = bl = arg3.hasOutline() && arg3.invisible;
        if (arg3.invisible && !bl) {
            return;
        }
        BlockState lv = arg3.type.getMushroomState();
        int j = LivingEntityRenderer.getOverlay(arg3, 0.0f);
        BlockStateModel lv2 = this.blockRenderManager.getModel(lv);
        arg.push();
        arg.translate(0.2f, -0.35f, 0.5f);
        arg.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-48.0f));
        arg.scale(-1.0f, -1.0f, 1.0f);
        arg.translate(-0.5f, -0.5f, -0.5f);
        this.renderMushroom(arg, arg2, i, bl, arg3.outlineColor, lv, j, lv2);
        arg.pop();
        arg.push();
        arg.translate(0.2f, -0.35f, 0.5f);
        arg.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(42.0f));
        arg.translate(0.1f, 0.0f, -0.6f);
        arg.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-48.0f));
        arg.scale(-1.0f, -1.0f, 1.0f);
        arg.translate(-0.5f, -0.5f, -0.5f);
        this.renderMushroom(arg, arg2, i, bl, arg3.outlineColor, lv, j, lv2);
        arg.pop();
        arg.push();
        ((CowEntityModel)this.getContextModel()).getHead().applyTransform(arg);
        arg.translate(0.0f, -0.7f, -0.2f);
        arg.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-78.0f));
        arg.scale(-1.0f, -1.0f, 1.0f);
        arg.translate(-0.5f, -0.5f, -0.5f);
        this.renderMushroom(arg, arg2, i, bl, arg3.outlineColor, lv, j, lv2);
        arg.pop();
    }

    private void renderMushroom(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, boolean renderAsModel, int outlineColor, BlockState mushroom, int overlay, BlockStateModel mushroomModel) {
        if (renderAsModel) {
            queue.submitBlockStateModel(matrices, RenderLayers.outlineNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE), mushroomModel, 0.0f, 0.0f, 0.0f, light, overlay, outlineColor);
        } else {
            queue.submitBlock(matrices, mushroom, light, overlay, outlineColor);
        }
    }
}

