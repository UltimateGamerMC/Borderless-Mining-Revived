/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPart;applyTransform(Lnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlock(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/block/BlockState;III)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/IronGolemFlowerFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/IronGolemEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.render.entity.state.IronGolemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class IronGolemFlowerFeatureRenderer
extends FeatureRenderer<IronGolemEntityRenderState, IronGolemEntityModel> {
    public IronGolemFlowerFeatureRenderer(FeatureRendererContext<IronGolemEntityRenderState, IronGolemEntityModel> arg) {
        super(arg);
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, IronGolemEntityRenderState arg3, float f, float g) {
        if (arg3.lookingAtVillagerTicks == 0) {
            return;
        }
        arg.push();
        ModelPart lv = ((IronGolemEntityModel)this.getContextModel()).getRightArm();
        lv.applyTransform(arg);
        arg.translate(-1.1875f, 1.0625f, -0.9375f);
        arg.translate(0.5f, 0.5f, 0.5f);
        float h = 0.5f;
        arg.scale(0.5f, 0.5f, 0.5f);
        arg.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
        arg.translate(-0.5f, -0.5f, -0.5f);
        arg2.submitBlock(arg, Blocks.POPPY.getDefaultState(), i, OverlayTexture.DEFAULT_UV, arg3.outlineColor);
        arg.pop();
    }
}

