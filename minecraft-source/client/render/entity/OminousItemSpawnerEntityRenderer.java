/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/EntityRenderer;updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;update(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/item/ItemModelManager;)V
 *   Lnet/minecraft/client/render/entity/ItemEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;Lnet/minecraft/util/math/random/Random;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/OminousItemSpawnerEntityRenderer;updateRenderState(Lnet/minecraft/entity/OminousItemSpawnerEntity;Lnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/OminousItemSpawnerEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;
 *   Lnet/minecraft/client/render/entity/OminousItemSpawnerEntityRenderer;render(Lnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.OminousItemSpawnerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

@Environment(value=EnvType.CLIENT)
public class OminousItemSpawnerEntityRenderer
extends EntityRenderer<OminousItemSpawnerEntity, ItemStackEntityRenderState> {
    private static final float field_50231 = 40.0f;
    private static final int field_50232 = 50;
    private final ItemModelManager itemModelManager;
    private final Random random = Random.create();

    protected OminousItemSpawnerEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.itemModelManager = arg.getItemModelManager();
    }

    @Override
    public ItemStackEntityRenderState createRenderState() {
        return new ItemStackEntityRenderState();
    }

    @Override
    public void updateRenderState(OminousItemSpawnerEntity arg, ItemStackEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ItemStack lv = arg.getItem();
        arg2.update(arg, lv, this.itemModelManager);
    }

    @Override
    public void render(ItemStackEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        float f;
        if (arg.itemRenderState.isEmpty()) {
            return;
        }
        arg2.push();
        if (arg.age <= 50.0f) {
            f = Math.min(arg.age, 50.0f) / 50.0f;
            arg2.scale(f, f, f);
        }
        f = MathHelper.wrapDegrees(arg.age * 40.0f);
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));
        ItemEntityRenderer.render(arg2, arg3, 0xF000F0, arg, this.random);
        arg2.pop();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

