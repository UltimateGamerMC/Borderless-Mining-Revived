/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;entityRenderDispatcher()Lnet/minecraft/client/render/entity/EntityRenderManager;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/MobSpawnerBlockEntityRenderer;renderDisplayEntity(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/entity/EntityRenderManager;FFLnet/minecraft/client/render/state/CameraRenderState;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/TrialSpawnerBlockEntityRenderer;updateSpawnerRenderState(Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;FLnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/EntityRenderManager;DD)V
 *   Lnet/minecraft/client/render/block/entity/TrialSpawnerBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/TrialSpawnerBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/TrialSpawnerBlockEntity;Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/TrialSpawnerBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/MobSpawnerBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.MobSpawnerBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class TrialSpawnerBlockEntityRenderer
implements BlockEntityRenderer<TrialSpawnerBlockEntity, MobSpawnerBlockEntityRenderState> {
    private final EntityRenderManager entityRenderDispatcher;

    public TrialSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.entityRenderDispatcher = context.entityRenderDispatcher();
    }

    @Override
    public MobSpawnerBlockEntityRenderState createRenderState() {
        return new MobSpawnerBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(TrialSpawnerBlockEntity arg, MobSpawnerBlockEntityRenderState arg2, float f, Vec3d arg3, @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        if (arg.getWorld() == null) {
            return;
        }
        TrialSpawnerLogic lv = arg.getSpawner();
        TrialSpawnerData lv2 = lv.getData();
        Entity lv3 = lv2.setDisplayEntity(lv, arg.getWorld(), lv.getSpawnerState());
        TrialSpawnerBlockEntityRenderer.updateSpawnerRenderState(arg2, f, lv3, this.entityRenderDispatcher, lv2.getLastDisplayEntityRotation(), lv2.getDisplayEntityRotation());
    }

    static void updateSpawnerRenderState(MobSpawnerBlockEntityRenderState state, float tickProgress, @Nullable Entity displayEntity, EntityRenderManager entityRenderDispatcher, double lastDisplayEntityRotation, double displayEntityRotation) {
        if (displayEntity == null) {
            return;
        }
        state.displayEntityRenderState = entityRenderDispatcher.getAndUpdateRenderState(displayEntity, tickProgress);
        state.displayEntityRenderState.light = state.lightmapCoordinates;
        state.displayEntityRotation = (float)MathHelper.lerp((double)tickProgress, lastDisplayEntityRotation, displayEntityRotation) * 10.0f;
        state.displayEntityScale = 0.53125f;
        float g = Math.max(displayEntity.getWidth(), displayEntity.getHeight());
        if ((double)g > 1.0) {
            state.displayEntityScale /= g;
        }
    }

    @Override
    public void render(MobSpawnerBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.displayEntityRenderState != null) {
            MobSpawnerBlockEntityRenderer.renderDisplayEntity(arg2, arg3, arg.displayEntityRenderState, this.entityRenderDispatcher, arg.displayEntityRotation, arg.displayEntityScale, arg4);
        }
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

