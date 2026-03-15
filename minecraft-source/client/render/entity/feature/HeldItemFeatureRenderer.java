/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/state/Lancing;method_75395(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;method_75382(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/HeldItemFeatureRenderer;renderItem(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V
 *   Lnet/minecraft/client/render/entity/feature/HeldItemFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.Lancing;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.SwingAnimationType;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class HeldItemFeatureRenderer<S extends ArmedEntityRenderState, M extends EntityModel<S>>
extends FeatureRenderer<S, M> {
    public HeldItemFeatureRenderer(FeatureRendererContext<S, M> arg) {
        super(arg);
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, S arg3, float f, float g) {
        this.renderItem(arg3, ((ArmedEntityRenderState)arg3).rightHandItemState, ((ArmedEntityRenderState)arg3).rightHandItem, Arm.RIGHT, arg, arg2, i);
        this.renderItem(arg3, ((ArmedEntityRenderState)arg3).leftHandItemState, ((ArmedEntityRenderState)arg3).leftHandItem, Arm.LEFT, arg, arg2, i);
    }

    protected void renderItem(S entityState, ItemRenderState itemState, ItemStack stack, Arm arm, MatrixStack matrices, OrderedRenderCommandQueue queue, int light) {
        float f;
        if (itemState.isEmpty()) {
            return;
        }
        matrices.push();
        ((ModelWithArms)this.getContextModel()).setArmAngle(entityState, arm, matrices);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        boolean bl = arm == Arm.LEFT;
        matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125f, -0.625f);
        if (((ArmedEntityRenderState)entityState).handSwingProgress > 0.0f && ((ArmedEntityRenderState)entityState).mainArm == arm && ((ArmedEntityRenderState)entityState).swingAnimationType == SwingAnimationType.STAB) {
            Lancing.method_75395(entityState, matrices);
        }
        if ((f = ((ArmedEntityRenderState)entityState).getItemUseTime(arm)) != 0.0f) {
            (arm == Arm.RIGHT ? ((ArmedEntityRenderState)entityState).rightArmPose : ((ArmedEntityRenderState)entityState).leftArmPose).method_75382(entityState, matrices, f, arm, stack);
        }
        itemState.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, ((ArmedEntityRenderState)entityState).outlineColor);
        matrices.pop();
    }
}

