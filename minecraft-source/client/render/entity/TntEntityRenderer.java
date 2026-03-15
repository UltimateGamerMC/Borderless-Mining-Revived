/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderFlashingBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IZI)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/TntEntityRenderer;updateRenderState(Lnet/minecraft/entity/TntEntity;Lnet/minecraft/client/render/entity/state/TntEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/TntEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/TntEntityRenderState;
 *   Lnet/minecraft/client/render/entity/TntEntityRenderer;render(Lnet/minecraft/client/render/entity/state/TntEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class TntEntityRenderer
extends EntityRenderer<TntEntity, TntEntityRenderState> {
    public TntEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(TntEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        arg2.push();
        arg2.translate(0.0f, 0.5f, 0.0f);
        float f = arg.fuse;
        if (arg.fuse < 10.0f) {
            float g = 1.0f - arg.fuse / 10.0f;
            g = MathHelper.clamp(g, 0.0f, 1.0f);
            g *= g;
            g *= g;
            float h = 1.0f + g * 0.3f;
            arg2.scale(h, h, h);
        }
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f));
        arg2.translate(-0.5f, -0.5f, 0.5f);
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        if (arg.blockState != null) {
            TntMinecartEntityRenderer.renderFlashingBlock(arg.blockState, arg2, arg3, arg.light, (int)f / 5 % 2 == 0, arg.outlineColor);
        }
        arg2.pop();
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public TntEntityRenderState createRenderState() {
        return new TntEntityRenderState();
    }

    @Override
    public void updateRenderState(TntEntity arg, TntEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.fuse = (float)arg.getFuse() - f + 1.0f;
        arg2.blockState = arg.getBlockState();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

