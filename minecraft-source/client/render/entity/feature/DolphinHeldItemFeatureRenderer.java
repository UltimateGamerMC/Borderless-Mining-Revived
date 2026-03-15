/*
 * External method calls:
 *   Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/DolphinHeldItemFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/DolphinEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.DolphinEntityModel;
import net.minecraft.client.render.entity.state.DolphinEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class DolphinHeldItemFeatureRenderer
extends FeatureRenderer<DolphinEntityRenderState, DolphinEntityModel> {
    public DolphinHeldItemFeatureRenderer(FeatureRendererContext<DolphinEntityRenderState, DolphinEntityModel> arg) {
        super(arg);
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, DolphinEntityRenderState arg3, float f, float g) {
        ItemRenderState lv = arg3.itemRenderState;
        if (lv.isEmpty()) {
            return;
        }
        arg.push();
        float h = 1.0f;
        float j = -1.0f;
        float k = MathHelper.abs(arg3.pitch) / 60.0f;
        if (arg3.pitch < 0.0f) {
            arg.translate(0.0f, 1.0f - k * 0.5f, -1.0f + k * 0.5f);
        } else {
            arg.translate(0.0f, 1.0f + k * 0.8f, -1.0f + k * 0.2f);
        }
        lv.render(arg, arg2, i, OverlayTexture.DEFAULT_UV, arg3.outlineColor);
        arg.pop();
    }
}

