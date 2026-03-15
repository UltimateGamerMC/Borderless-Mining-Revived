/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;updateBlockEntityRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/BeaconBlockEntityRenderer;updateBeaconRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BeaconBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;updateStructureBoxRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;)V
 *   Lnet/minecraft/block/entity/TestInstanceBlockEntity$Error;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/block/entity/TestInstanceBlockEntity$Error;text()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/render/block/entity/BeaconBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/BeaconBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/TestInstanceBlockEntityRenderer;renderError(Lnet/minecraft/block/entity/TestInstanceBlockEntity$Error;)V
 *   Lnet/minecraft/client/render/block/entity/TestInstanceBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/TestInstanceBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/TestInstanceBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/TestInstanceBlockEntity;Lnet/minecraft/client/render/block/entity/state/TestInstanceBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/TestInstanceBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/TestInstanceBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.TestInstanceBlockEntity;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BeaconBlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.StructureBlockBlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.TestInstanceBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class TestInstanceBlockEntityRenderer
implements BlockEntityRenderer<TestInstanceBlockEntity, TestInstanceBlockEntityRenderState> {
    private static final float field_62965 = 0.02f;
    private final BeaconBlockEntityRenderer<TestInstanceBlockEntity> beaconBlockEntityRenderer = new BeaconBlockEntityRenderer();
    private final StructureBlockBlockEntityRenderer<TestInstanceBlockEntity> structureBlockBlockEntityRenderer = new StructureBlockBlockEntityRenderer();

    @Override
    public TestInstanceBlockEntityRenderState createRenderState() {
        return new TestInstanceBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(TestInstanceBlockEntity arg, TestInstanceBlockEntityRenderState arg2, float f, Vec3d arg3, @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.beaconState = new BeaconBlockEntityRenderState();
        BlockEntityRenderState.updateBlockEntityRenderState(arg, arg2.beaconState, arg4);
        BeaconBlockEntityRenderer.updateBeaconRenderState(arg, arg2.beaconState, f, arg3);
        arg2.structureState = new StructureBlockBlockEntityRenderState();
        BlockEntityRenderState.updateBlockEntityRenderState(arg, arg2.structureState, arg4);
        StructureBlockBlockEntityRenderer.updateStructureBoxRenderState(arg, arg2.structureState);
        arg2.errors.clear();
        for (TestInstanceBlockEntity.Error lv : arg.getErrors()) {
            arg2.errors.add(new TestInstanceBlockEntity.Error(lv.pos(), lv.text()));
        }
    }

    @Override
    public void render(TestInstanceBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        this.beaconBlockEntityRenderer.render(arg.beaconState, arg2, arg3, arg4);
        this.structureBlockBlockEntityRenderer.render(arg.structureState, arg2, arg3, arg4);
        for (TestInstanceBlockEntity.Error lv : arg.errors) {
            this.renderError(lv);
        }
    }

    private void renderError(TestInstanceBlockEntity.Error error) {
        BlockPos lv = error.pos();
        GizmoDrawing.box(new Box(lv).expand(0.02f), DrawStyle.filled(ColorHelper.fromFloats(0.375f, 1.0f, 0.0f, 0.0f)));
        String string = error.text().getString();
        float f = 0.16f;
        GizmoDrawing.text(string, Vec3d.add(lv, 0.5, 1.2, 0.5), TextGizmo.Style.left().scaled(0.16f)).ignoreOcclusion();
    }

    @Override
    public boolean rendersOutsideBoundingBox() {
        return this.beaconBlockEntityRenderer.rendersOutsideBoundingBox() || this.structureBlockBlockEntityRenderer.rendersOutsideBoundingBox();
    }

    @Override
    public int getRenderDistance() {
        return Math.max(this.beaconBlockEntityRenderer.getRenderDistance(), this.structureBlockBlockEntityRenderer.getRenderDistance());
    }

    @Override
    public boolean isInRenderDistance(TestInstanceBlockEntity arg, Vec3d arg2) {
        return this.beaconBlockEntityRenderer.isInRenderDistance(arg, arg2) || this.structureBlockBlockEntityRenderer.isInRenderDistance(arg, arg2);
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

