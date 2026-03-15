/*
 * External method calls:
 *   Lnet/minecraft/client/render/OverlayTexture;packUv(II)I
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlock(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/block/BlockState;III)V
 *   Lnet/minecraft/client/render/entity/AbstractMinecartEntityRenderer;updateRenderState(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderFlashingBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IZI)V
 *   Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderBlock(Lnet/minecraft/client/render/entity/state/TntMinecartEntityRenderState;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V
 *   Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;updateRenderState(Lnet/minecraft/entity/vehicle/TntMinecartEntity;Lnet/minecraft/client/render/entity/state/TntMinecartEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/TntMinecartEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.TntMinecartEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class TntMinecartEntityRenderer
extends AbstractMinecartEntityRenderer<TntMinecartEntity, TntMinecartEntityRenderState> {
    public TntMinecartEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, EntityModelLayers.TNT_MINECART);
    }

    @Override
    protected void renderBlock(TntMinecartEntityRenderState arg, BlockState arg2, MatrixStack arg3, OrderedRenderCommandQueue arg4, int i) {
        float f = arg.fuseTicks;
        if (f > -1.0f && f < 10.0f) {
            float g = 1.0f - f / 10.0f;
            g = MathHelper.clamp(g, 0.0f, 1.0f);
            g *= g;
            g *= g;
            float h = 1.0f + g * 0.3f;
            arg3.scale(h, h, h);
        }
        TntMinecartEntityRenderer.renderFlashingBlock(arg2, arg3, arg4, i, f > -1.0f && (int)f / 5 % 2 == 0, arg.outlineColor);
    }

    public static void renderFlashingBlock(BlockState state, MatrixStack matrices, OrderedRenderCommandQueue queue, int i, boolean bl, int j) {
        int k = bl ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV;
        queue.submitBlock(matrices, state, i, k, j);
    }

    @Override
    public TntMinecartEntityRenderState createRenderState() {
        return new TntMinecartEntityRenderState();
    }

    @Override
    public void updateRenderState(TntMinecartEntity arg, TntMinecartEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.fuseTicks = arg.getFuseTicks() > -1 ? (float)arg.getFuseTicks() - f + 1.0f : -1.0f;
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

