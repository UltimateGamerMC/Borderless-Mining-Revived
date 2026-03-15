/*
 * External method calls:
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitMovingBlock(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/block/MovingBlockRenderState;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/FallingBlockEntityRenderer;updateRenderState(Lnet/minecraft/entity/FallingBlockEntity;Lnet/minecraft/client/render/entity/state/FallingBlockEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/FallingBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/FallingBlockEntityRenderState;
 *   Lnet/minecraft/client/render/entity/FallingBlockEntityRenderer;render(Lnet/minecraft/client/render/entity/state/FallingBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.FallingBlockEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;

@Environment(value=EnvType.CLIENT)
public class FallingBlockEntityRenderer
extends EntityRenderer<FallingBlockEntity, FallingBlockEntityRenderState> {
    public FallingBlockEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.shadowRadius = 0.5f;
    }

    @Override
    public boolean shouldRender(FallingBlockEntity arg, Frustum arg2, double d, double e, double f) {
        if (!super.shouldRender(arg, arg2, d, e, f)) {
            return false;
        }
        return arg.getBlockState() != arg.getEntityWorld().getBlockState(arg.getBlockPos());
    }

    @Override
    public void render(FallingBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        BlockState lv = arg.movingBlockRenderState.blockState;
        if (lv.getRenderType() != BlockRenderType.MODEL) {
            return;
        }
        arg2.push();
        arg2.translate(-0.5, 0.0, -0.5);
        arg3.submitMovingBlock(arg2, arg.movingBlockRenderState);
        arg2.pop();
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public FallingBlockEntityRenderState createRenderState() {
        return new FallingBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(FallingBlockEntity arg, FallingBlockEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        BlockPos lv = BlockPos.ofFloored(arg.getX(), arg.getBoundingBox().maxY, arg.getZ());
        arg2.movingBlockRenderState.fallingBlockPos = arg.getFallingBlockPos();
        arg2.movingBlockRenderState.entityBlockPos = lv;
        arg2.movingBlockRenderState.blockState = arg.getBlockState();
        arg2.movingBlockRenderState.biome = arg.getEntityWorld().getBiome(lv);
        arg2.movingBlockRenderState.world = arg.getEntityWorld();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

