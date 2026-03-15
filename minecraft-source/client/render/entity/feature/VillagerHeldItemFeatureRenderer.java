/*
 * External method calls:
 *   Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V
 *   Lnet/minecraft/client/render/entity/model/ModelWithHat;rotateArms(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/VillagerHeldItemFeatureRenderer;applyTransforms(Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/entity/feature/VillagerHeldItemFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class VillagerHeldItemFeatureRenderer<S extends ItemHolderEntityRenderState, M extends EntityModel<S>>
extends FeatureRenderer<S, M> {
    public VillagerHeldItemFeatureRenderer(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, S arg3, float f, float g) {
        ItemRenderState lv = ((ItemHolderEntityRenderState)arg3).itemRenderState;
        if (lv.isEmpty()) {
            return;
        }
        arg.push();
        this.applyTransforms(arg3, arg);
        lv.render(arg, arg2, i, OverlayTexture.DEFAULT_UV, ((ItemHolderEntityRenderState)arg3).outlineColor);
        arg.pop();
    }

    protected void applyTransforms(S state, MatrixStack matrices) {
        ((ModelWithHat)this.getContextModel()).rotateArms(state, matrices);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(0.75f));
        matrices.scale(1.07f, 1.07f, 1.07f);
        matrices.translate(0.0f, 0.13f, -0.34f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)Math.PI));
    }
}

