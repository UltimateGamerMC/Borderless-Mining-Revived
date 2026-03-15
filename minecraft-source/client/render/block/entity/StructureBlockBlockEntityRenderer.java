/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/block/entity/StructureBoxRendering$StructureBox;localPos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;Z)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/util/shape/VoxelSet;forEachDirection(Lnet/minecraft/util/shape/VoxelSet$PositionConsumer;)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;face(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;updateStructureBoxRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;renderInvisibleBlocks(Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3i;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/StructureBlockBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/StructureBlockBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBoxRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.StructureBlockBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class StructureBlockBlockEntityRenderer<T extends BlockEntity>
implements BlockEntityRenderer<T, StructureBlockBlockEntityRenderState> {
    public static final int field_63584 = ColorHelper.fromFloats(0.2f, 0.75f, 0.75f, 1.0f);

    @Override
    public StructureBlockBlockEntityRenderState createRenderState() {
        return new StructureBlockBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T arg, StructureBlockBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        StructureBlockBlockEntityRenderer.updateStructureBoxRenderState(arg, arg2);
    }

    public static <T extends BlockEntity> void updateStructureBoxRenderState(T blockEntity, StructureBlockBlockEntityRenderState state) {
        ClientPlayerEntity lv = MinecraftClient.getInstance().player;
        state.visible = lv.isCreativeLevelTwoOp() || lv.isSpectator();
        state.structureBox = ((StructureBoxRendering)((Object)blockEntity)).getStructureBox();
        state.renderMode = ((StructureBoxRendering)((Object)blockEntity)).getRenderMode();
        BlockPos lv2 = state.structureBox.localPos();
        Vec3i lv3 = state.structureBox.size();
        BlockPos lv4 = state.pos;
        BlockPos lv5 = lv4.add(lv2);
        if (state.visible && blockEntity.getWorld() != null && state.renderMode == StructureBoxRendering.RenderMode.BOX_AND_INVISIBLE_BLOCKS) {
            state.invisibleBlocks = new StructureBlockBlockEntityRenderState.InvisibleRenderType[lv3.getX() * lv3.getY() * lv3.getZ()];
            for (int i = 0; i < lv3.getX(); ++i) {
                for (int j = 0; j < lv3.getY(); ++j) {
                    for (int k = 0; k < lv3.getZ(); ++k) {
                        int l = k * lv3.getX() * lv3.getY() + j * lv3.getX() + i;
                        BlockState lv6 = blockEntity.getWorld().getBlockState(lv5.add(i, j, k));
                        if (lv6.isAir()) {
                            state.invisibleBlocks[l] = StructureBlockBlockEntityRenderState.InvisibleRenderType.AIR;
                            continue;
                        }
                        if (lv6.isOf(Blocks.STRUCTURE_VOID)) {
                            state.invisibleBlocks[l] = StructureBlockBlockEntityRenderState.InvisibleRenderType.STRUCTURE_VOID;
                            continue;
                        }
                        if (lv6.isOf(Blocks.BARRIER)) {
                            state.invisibleBlocks[l] = StructureBlockBlockEntityRenderState.InvisibleRenderType.BARRIER;
                            continue;
                        }
                        if (!lv6.isOf(Blocks.LIGHT)) continue;
                        state.invisibleBlocks[l] = StructureBlockBlockEntityRenderState.InvisibleRenderType.LIGHT;
                    }
                }
            }
        } else {
            state.invisibleBlocks = null;
        }
        if (state.visible) {
            // empty if block
        }
        state.field_62682 = null;
    }

    @Override
    public void render(StructureBlockBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (!arg.visible) {
            return;
        }
        StructureBoxRendering.RenderMode lv = arg.renderMode;
        if (lv == StructureBoxRendering.RenderMode.NONE) {
            return;
        }
        StructureBoxRendering.StructureBox lv2 = arg.structureBox;
        BlockPos lv3 = lv2.localPos();
        Vec3i lv4 = lv2.size();
        if (lv4.getX() < 1 || lv4.getY() < 1 || lv4.getZ() < 1) {
            return;
        }
        float f = 1.0f;
        float g = 0.9f;
        BlockPos lv5 = lv3.add(lv4);
        GizmoDrawing.box(new Box(lv3.getX(), lv3.getY(), lv3.getZ(), lv5.getX(), lv5.getY(), lv5.getZ()).offset(arg.pos), DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 0.9f, 0.9f, 0.9f)), true);
        this.renderInvisibleBlocks(arg, lv3, lv4);
    }

    private void renderInvisibleBlocks(StructureBlockBlockEntityRenderState state, BlockPos pos, Vec3i size) {
        if (state.invisibleBlocks == null) {
            return;
        }
        BlockPos lv = state.pos;
        BlockPos lv2 = lv.add(pos);
        for (int i = 0; i < size.getX(); ++i) {
            for (int j = 0; j < size.getY(); ++j) {
                for (int k = 0; k < size.getZ(); ++k) {
                    int l = k * size.getX() * size.getY() + j * size.getX() + i;
                    StructureBlockBlockEntityRenderState.InvisibleRenderType lv3 = state.invisibleBlocks[l];
                    if (lv3 == null) continue;
                    float f = lv3 == StructureBlockBlockEntityRenderState.InvisibleRenderType.AIR ? 0.05f : 0.0f;
                    double d = (float)(lv2.getX() + i) + 0.45f - f;
                    double e = (float)(lv2.getY() + j) + 0.45f - f;
                    double g = (float)(lv2.getZ() + k) + 0.45f - f;
                    double h = (float)(lv2.getX() + i) + 0.55f + f;
                    double m = (float)(lv2.getY() + j) + 0.55f + f;
                    double n = (float)(lv2.getZ() + k) + 0.55f + f;
                    Box lv4 = new Box(d, e, g, h, m, n);
                    if (lv3 == StructureBlockBlockEntityRenderState.InvisibleRenderType.AIR) {
                        GizmoDrawing.box(lv4, DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 0.5f, 0.5f, 1.0f)));
                        continue;
                    }
                    if (lv3 == StructureBlockBlockEntityRenderState.InvisibleRenderType.STRUCTURE_VOID) {
                        GizmoDrawing.box(lv4, DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 1.0f, 0.75f, 0.75f)));
                        continue;
                    }
                    if (lv3 == StructureBlockBlockEntityRenderState.InvisibleRenderType.BARRIER) {
                        GizmoDrawing.box(lv4, DrawStyle.stroked(-65536));
                        continue;
                    }
                    if (lv3 != StructureBlockBlockEntityRenderState.InvisibleRenderType.LIGHT) continue;
                    GizmoDrawing.box(lv4, DrawStyle.stroked(-256));
                }
            }
        }
    }

    private void renderStructureVoids(StructureBlockBlockEntityRenderState state, BlockPos pos, Vec3i size) {
        if (state.field_62682 == null) {
            return;
        }
        BitSetVoxelSet lv = new BitSetVoxelSet(size.getX(), size.getY(), size.getZ());
        for (int i2 = 0; i2 < size.getX(); ++i2) {
            for (int j2 = 0; j2 < size.getY(); ++j2) {
                for (int k2 = 0; k2 < size.getZ(); ++k2) {
                    int l = k2 * size.getX() * size.getY() + j2 * size.getX() + i2;
                    if (!state.field_62682[l]) continue;
                    ((VoxelSet)lv).set(i2, j2, k2);
                }
            }
        }
        lv.forEachDirection((arg2, i, j, k) -> {
            float f = 0.48f;
            float g = (float)(i + pos.getX()) + 0.5f - 0.48f;
            float h = (float)(j + pos.getY()) + 0.5f - 0.48f;
            float l = (float)(k + pos.getZ()) + 0.5f - 0.48f;
            float m = (float)(i + pos.getX()) + 0.5f + 0.48f;
            float n = (float)(j + pos.getY()) + 0.5f + 0.48f;
            float o = (float)(k + pos.getZ()) + 0.5f + 0.48f;
            GizmoDrawing.face(new Vec3d(g, h, l), new Vec3d(m, n, o), arg2, DrawStyle.filled(field_63584));
        });
    }

    @Override
    public boolean rendersOutsideBoundingBox() {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

