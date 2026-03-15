/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;entityRenderDispatcher()Lnet/minecraft/client/render/entity/EntityRenderManager;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/TrialSpawnerBlockEntityRenderer;updateSpawnerRenderState(Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;FLnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/EntityRenderManager;DD)V
 *   Lnet/minecraft/client/render/entity/EntityRenderManager;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/MobSpawnerBlockEntityRenderer;renderDisplayEntity(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/entity/EntityRenderManager;FFLnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/MobSpawnerBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/MobSpawnerBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/MobSpawnerBlockEntity;Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/MobSpawnerBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.TrialSpawnerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.MobSpawnerBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class MobSpawnerBlockEntityRenderer
implements BlockEntityRenderer<MobSpawnerBlockEntity, MobSpawnerBlockEntityRenderState> {
    private final EntityRenderManager entityRenderDispatcher;

    public MobSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.entityRenderDispatcher = ctx.entityRenderDispatcher();
    }

    @Override
    public MobSpawnerBlockEntityRenderState createRenderState() {
        return new MobSpawnerBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(MobSpawnerBlockEntity arg, MobSpawnerBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        if (arg.getWorld() == null) {
            return;
        }
        MobSpawnerLogic lv = arg.getLogic();
        Entity lv2 = lv.getRenderedEntity(arg.getWorld(), arg.getPos());
        TrialSpawnerBlockEntityRenderer.updateSpawnerRenderState(arg2, f, lv2, this.entityRenderDispatcher, lv.getLastRotation(), lv.getRotation());
    }

    @Override
    public void render(MobSpawnerBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.displayEntityRenderState != null) {
            MobSpawnerBlockEntityRenderer.renderDisplayEntity(arg2, arg3, arg.displayEntityRenderState, this.entityRenderDispatcher, arg.displayEntityRotation, arg.displayEntityScale, arg4);
        }
    }

    public static void renderDisplayEntity(MatrixStack matrices, OrderedRenderCommandQueue queue, EntityRenderState state, EntityRenderManager entityRenderDispatcher, float rotation, float scale, CameraRenderState cameraRenderState) {
        matrices.push();
        matrices.translate(0.5f, 0.4f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrices.translate(0.0f, -0.2f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0f));
        matrices.scale(scale, scale, scale);
        entityRenderDispatcher.render(state, cameraRenderState, 0.0, 0.0, 0.0, matrices, queue);
        matrices.pop();
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

